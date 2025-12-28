package com.kneelawk.krender.engine.base.buffer;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.Nullable;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.TriState;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.buffer.PooledQuadEmitter;
import com.kneelawk.krender.engine.api.buffer.PooledVertexEmitter;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.buffer.QuadTransform;
import com.kneelawk.krender.engine.api.buffer.QuadView;
import com.kneelawk.krender.engine.api.buffer.VertexEmitter;
import com.kneelawk.krender.engine.api.texture.MaterialTexture;

import static com.kneelawk.krender.engine.api.util.ColorUtils.toArgb;
import static com.kneelawk.krender.engine.api.util.ColorUtils.toFixed;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_COLOR;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_LIGHTMAP;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_NORMAL;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_STRIDE;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_U;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_V;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_X;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_Y;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_Z;

/**
 * Base {@link QuadEmitter} implementation for use in backends.
 */
public abstract class BaseQuadEmitter extends BaseQuadView implements QuadEmitter, VertexEmitter {
    /**
     * The current vertex index of this vertex emitter.
     */
    protected int vertexIndex = 0;

    /**
     * Whether we are currently building a vertex.
     */
    protected boolean buildingVertex = false;

    private final Vector3f sortNormal = new Vector3f();
    private final Vector3f sortTangent = new Vector3f();
    private final Vector3f sortBinormal = new Vector3f();
    private final int[] sortData;

    /**
     * Creates a new base quad emitter associated with the given KRenderer.
     *
     * @param renderer the KRenderer to associate this quad emitter with.
     */
    public BaseQuadEmitter(KRenderer renderer) {
        super(renderer);
        sortData = new int[format.totalStride];
    }

    /**
     * Initializes this quad emitter to use the given array, but makes no assumptions about the validity of any data
     * existing in the buffer, unlike {@link #load(int[], int)}.
     *
     * @param data      the new buffer to use.
     * @param baseIndex the new base index to use.
     */
    public void begin(int[] data, int baseIndex) {
        this.data = data;
        this.baseIndex = baseIndex;
        clear();
    }

    /**
     * Resets the quad data.
     */
    public void clear() {
        System.arraycopy(format.empty, 0, data, baseIndex, format.totalStride);
        geometryInvalid = true;
        nominalFace = null;
        setTintIndex(-1);
        setCullFace(null);
        setRenderLayer(null);
        setAmbientOcclusionMode(TriState.DEFAULT);
        setFoilType(null);
        setTexture(renderer.textureManager().blockAtlas());
        vertexIndex = 0;
        buildingVertex = false;
    }

    /**
     * Called by {@link #emit()}. This is where implementations should handle the emitted geometry.
     * <p>
     * Note, geometry flags should be valid when this is called.
     */
    public abstract void emitDirectly();

    @Override
    public QuadEmitter emit() {
        buildingVertex = false;
        vertexIndex = 0;
        computeGeometry();
        emitDirectly();
        clear();
        return this;
    }

    /**
     * Directly copies a quad's data to this emitter's buffer.
     *
     * @param data      the buffer to copy from.
     * @param baseIndex the position within the buffer to start copying.
     */
    public void copyFrom(int[] data, int baseIndex) {
        Objects.requireNonNull(data, "data is null");
        Objects.requireNonNull(this.data, "this.data is null");
        System.arraycopy(data, baseIndex, this.data, this.baseIndex, format.totalStride);
        load();
    }

    /**
     * Equivalent to {@link #copyTo(QuadEmitter)} except that it puts the onus of copying on this quad emitter instead
     * of on the quad view being copied from.
     *
     * @param quad the quad view to copy from.
     */
    public void copyFrom(QuadView quad) {
        if (quad instanceof BaseQuadView base) {
            base.computeGeometry();
            load(base.data, base.baseIndex);
        } else {
            Vector3f vec3 = scratch3;
            Vector2f vec2 = scratch2;

            setCullFace(quad.getCullFace());
            setNominalFace(quad.getNominalFace());
            setRenderLayer(quad.getRenderLayer());
            setEmissive(quad.isEmissive());
            setDiffuseDisabled(quad.isDiffuseDisabled());
            setAmbientOcclusionMode(quad.getAmbientOcclusionMode());
            setFoilType(quad.getFoilType());
            setTexture(renderer.converter().toAssociated(quad.getTexture()));
            setTintIndex(quad.getTintIndex());
            setTag(quad.getTag());

            for (int i = 0; i < 4; i++) {
                quad.copyPos(i, vec3);
                setPos(i, vec3);

                setColor(i, quad.getColor(i));

                quad.copyUv(i, vec2);
                setUv(i, vec2);

                setLightmap(i, quad.getLightmap(i));

                if (quad.hasNormal(i)) {
                    quad.copyNormal(i, vec3);
                    setNormal(i, vec3);
                } else {
                    removeNormal(i);
                }
            }
        }
    }

    @Override
    public QuadEmitter setPos(int vertexIndex, float x, float y, float z) {
        flushVertices();
        return setPosImpl(vertexIndex, x, y, z);
    }

    /**
     * Version of {@link #setPos(int, float, float, float)} that does not flush vertices.
     *
     * @param vertexIndex the index of the vertex to position.
     * @param x           the x position.
     * @param y           the y position.
     * @param z           the z position.
     * @return this quad emitter.
     */
    protected @NotNull BaseQuadEmitter setPosImpl(int vertexIndex, float x, float y, float z) {
        final int index = baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_X;
        data[index] = Float.floatToRawIntBits(x);
        data[index + 1] = Float.floatToRawIntBits(y);
        data[index + 2] = Float.floatToRawIntBits(z);
        geometryInvalid = true;
        return this;
    }

    @Override
    public QuadEmitter setPosByIndex(int vertexIndex, int coordinateIndex, float value) {
        flushVertices();
        data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_X + coordinateIndex] =
            Float.floatToRawIntBits(value);
        geometryInvalid = true;
        return this;
    }

    @Override
    public QuadEmitter setColor(int vertexIndex, int color) {
        flushVertices();
        return setColorImpl(vertexIndex, color);
    }

    /**
     * Version of {@link #setColor(int, int)} that does not flush vertices.
     *
     * @param vertexIndex the index of the vertex to color.
     * @param color       the color to set.
     * @return this quad emitter.
     */
    protected @NotNull BaseQuadEmitter setColorImpl(int vertexIndex, int color) {
        data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_COLOR] = color;
        return this;
    }

    @Override
    public QuadEmitter setUv(int vertexIndex, float u, float v) {
        flushVertices();
        return setUvImpl(vertexIndex, u, v);
    }

    /**
     * Version of {@link #setUv(int, float, float)} that does not flush vertices.
     *
     * @param vertexIndex the index of the vertex to set the uv of.
     * @param u           the u component.
     * @param v           the v component.
     * @return this quad emitter.
     */
    protected @NotNull BaseQuadEmitter setUvImpl(int vertexIndex, float u, float v) {
        final int index = baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_U;
        data[index] = Float.floatToRawIntBits(u);
        data[index + 1] = Float.floatToRawIntBits(v);
        return this;
    }

    @Override
    public QuadEmitter setUvByIndex(int vertexIndex, int coordinateIndex, float value) {
        flushVertices();
        data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_U + coordinateIndex] =
            Float.floatToRawIntBits(value);
        return this;
    }

    @Override
    public QuadEmitter spriteBake(TextureAtlasSprite sprite, int bakeFlags) {
        flushVertices();
        SpriteHelper.bakeSprite(this, sprite, bakeFlags);
        return this;
    }

    @Override
    public QuadEmitter setLightmap(int vertexIndex, int lightmap) {
        flushVertices();
        return setLightmapImpl(vertexIndex, lightmap);
    }

    /**
     * Version of {@link #setLightmap(int, int)} that does not flush vertices.
     *
     * @param vertexIndex the index of the vertex to set the lightmap of.
     * @param lightmap    the lightmap value to set.
     * @return this quad emitter.
     */
    protected @NotNull BaseQuadEmitter setLightmapImpl(int vertexIndex, int lightmap) {
        data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_LIGHTMAP] = lightmap;
        return this;
    }

    @Override
    public QuadEmitter setNormal(int vertexIndex, float x, float y, float z) {
        flushVertices();
        return setNormalImpl(vertexIndex, x, y, z);
    }

    /**
     * Version of {@link #setNormal(int, float, float, float)} that does not flush vertices.
     *
     * @param vertexIndex the index of the vertex to set the normal of.
     * @param x           the normal x component.
     * @param y           the normal y component.
     * @param z           the normal z component.
     * @return this quad emitter.
     */
    private @NotNull BaseQuadEmitter setNormalImpl(int vertexIndex, float x, float y, float z) {
        setNormal(vertexIndex);
        data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL] =
            NormalHelper.packNormal(x, y, z);
        return this;
    }

    @Override
    public QuadEmitter setNormalByIndex(int vertexIndex, int coordinateIndex, float value) {
        flushVertices();
        setNormal(vertexIndex);
        final int index = baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL;
        data[index] = NormalHelper.packNormal(data[index], value, coordinateIndex);
        return this;
    }

    @Override
    public QuadEmitter removeNormal(int vertexIndex) {
        flushVertices();
        final int index = baseIndex + format.headerBits;
        data[index] = format.setNormalPresent(data[index], vertexIndex, false);
        return this;
    }

    /**
     * Sets that the normal for the given vertex is indeed present.
     *
     * @param vertexIndex the index of the vertex to mark the normal as present for.
     */
    protected void setNormal(int vertexIndex) {
        final int index = baseIndex + format.headerBits;
        data[index] = format.setNormalPresent(data[index], vertexIndex, true);
    }

    @Override
    public QuadEmitter setCullFace(@Nullable Direction face) {
        flushVertices();
        final int index = baseIndex + format.headerBits;
        format.cull.setI(data, index, face);
        setNominalFace(face);
        return this;
    }

    @Override
    public QuadEmitter setNominalFace(@Nullable Direction face) {
        flushVertices();
        nominalFace = face;
        return this;
    }

    @Override
    public BaseQuadEmitter setRenderLayer(@Nullable ChunkSectionLayer renderLayer) {
        format.renderLayer.setI(data, baseIndex + format.headerBits, renderLayer);
        return this;
    }

    @Override
    public BaseQuadEmitter setEmissive(boolean emissive) {
        format.emissive.setI(data, baseIndex + format.headerBits, emissive);
        return this;
    }

    @Override
    public BaseQuadEmitter setDiffuseDisabled(boolean disabled) {
        format.diffuseDisabled.setI(data, baseIndex + format.headerBits, disabled);
        return this;
    }

    @Override
    public BaseQuadEmitter setAmbientOcclusionMode(TriState mode) {
        format.ambientOcclusion.setI(data, baseIndex + format.headerBits, mode);
        return this;
    }

    @Override
    public BaseQuadEmitter setFoilType(ItemStackRenderState.@Nullable FoilType foilType) {
        format.foilType.setI(data, baseIndex + format.headerBits, foilType);
        return this;
    }

    @Override
    public BaseQuadEmitter setTextureIntId(int textureIntId) {
        format.texture.setI(data, baseIndex + format.headerBits, textureIntId);
        return this;
    }

    @Override
    public BaseQuadEmitter setTexture(MaterialTexture texture) {
        return setTextureIntId(renderer.converter().toAssociated(texture).intId());
    }

    @Override
    public QuadEmitter setTintIndex(int tintIndex) {
        flushVertices();
        data[baseIndex + format.headerTintIndex] = tintIndex;
        return this;
    }

    @Override
    public QuadEmitter setTag(int tag) {
        flushVertices();
        data[baseIndex + format.headerTag] = tag;
        return this;
    }

    @Override
    public QuadEmitter fromVanilla(BakedQuad quad, @Nullable Direction cullFace) {
        flushVertices();

        for (int i = 0; i < 4; i++) {
            setPos(i, quad.position(i));
            long packedUv = quad.packedUV(i);
            setUv(i, UVPair.unpackU(packedUv), UVPair.unpackV(packedUv));
            setColor(i, -1);
            setLightmap(i, 0);
        }

        geometryInvalid = true;

        format.cull.setI(data, baseIndex + format.headerBits, cullFace);
        setNominalFace(quad.direction());
        setTintIndex(quad.tintIndex());

        // pick up shading from quad
        if (!quad.shade()) {
            setDiffuseDisabled(true);
        }

        setEmissive(quad.lightEmission() > 0);
        setTexture(renderer.textureManager().textureById(quad.sprite().atlasLocation()));

        setTag(0);

        return this;
    }

    @Override
    public QuadEmitter sortVertices(float normalX, float normalY, float normalZ, float binormalX, float binormalY,
                                    float binormalZ) {
        Vector3f normal = sortNormal;
        Vector3f tangent = sortTangent;
        Vector3f binormal = sortBinormal;
        int[] indices = {0, 1, 2, 3};
        float[] angles = new float[4];

        // these vectors don't seem to need to be normalized
        normal.set(normalX, normalY, normalZ);
        binormal.set(binormalX, binormalY, binormalZ);

        // get tangent
        normal.cross(binormal, tangent);
        if (tangent.lengthSquared() < EPSILON)
            throw new IllegalArgumentException("Normal and binormal vectors are parallel or zero");

        // get vector angles from center
        float cx = (getX(0) + getX(1) + getX(2) + getX(3)) / 4f;
        float cy = (getY(0) + getY(1) + getY(2) + getY(3)) / 4f;
        float cz = (getZ(0) + getZ(1) + getZ(2) + getZ(3)) / 4f;
        for (int i = 0; i < 4; i++) {
            float dx = getX(i) - cx;
            float dy = getY(i) - cy;
            float dz = getZ(i) - cz;
            // get angle starting from binormal axis, moving toward tangent axis, and going on around
            angles[i] = (float) Math.atan2(tangent.dot(dx, dy, dz), binormal.dot(dx, dy, dz));
            if (angles[i] < 0) angles[i] += (float) Math.PI * 2f;
        }

        // sort the indices by their angles
        if (angles[indices[0]] > angles[indices[2]]) swap(indices, 0, 2);
        if (angles[indices[1]] > angles[indices[3]]) swap(indices, 1, 3);
        if (angles[indices[0]] > angles[indices[1]]) swap(indices, 0, 1);
        if (angles[indices[2]] > angles[indices[3]]) swap(indices, 2, 3);
        if (angles[indices[1]] > angles[indices[2]]) swap(indices, 1, 2);

        // move vertices
        System.arraycopy(data, baseIndex, sortData, 0, format.totalStride);
        for (int i = 0; i < 4; i++) {
            System.arraycopy(sortData, format.headerStride + indices[i] * VERTEX_STRIDE, data,
                baseIndex + format.headerStride + i * VERTEX_STRIDE, VERTEX_STRIDE);
            format.normals[i].setI(data, baseIndex + format.headerBits,
                format.normals[indices[i]].getI(sortData, format.headerBits));
        }

        // mark geometry as invalid
        geometryInvalid = true;

        return this;
    }

    private static void swap(int[] a, int i1, int i2) {
        int s = a[i1];
        a[i1] = a[i2];
        a[i2] = s;
    }

    @Override
    public VertexEmitter addVertex(float x, float y, float z) {
        startVertex();
        setPosImpl(vertexIndex, x, y, z);
        return this;
    }

    @Override
    public VertexEmitter addVertex(Matrix4fc pose, float x, float y, float z) {
        final float px = x * pose.m00() + y * pose.m01() + z * pose.m02() + pose.m03();
        final float py = x * pose.m10() + y * pose.m11() + z * pose.m12() + pose.m13();
        final float pz = x * pose.m20() + y * pose.m21() + z * pose.m22() + pose.m23();

        startVertex();
        setPosImpl(vertexIndex, px, py, pz);
        return this;
    }

    @Override
    public VertexEmitter setColor(int red, int green, int blue, int alpha) {
        setColorImpl(vertexIndex,
            ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF));
        return this;
    }

    @Override
    public VertexEmitter setColor(int color) {
        // VertexConsumers use ARGB as well
        setColorImpl(vertexIndex, color);
        return this;
    }

    @Override
    public VertexEmitter setUv(float u, float v) {
        setUvImpl(vertexIndex, u, v);
        return this;
    }

    @Override
    public VertexEmitter setUv1(int u, int v) {
        // Not supported by default
        return this;
    }

    @Override
    public VertexEmitter setUv2(int u, int v) {
        setLightmapImpl(vertexIndex, (u & 0xFFFF) | ((v & 0xFFFF) << 16));
        return this;
    }

    @Override
    public VertexEmitter setLight(int packedLight) {
        setLightmapImpl(vertexIndex, packedLight);
        return this;
    }

    @Override
    public VertexEmitter setNormal(float normalX, float normalY, float normalZ) {
        setNormalImpl(vertexIndex, normalX, normalY, normalZ);
        return this;
    }

    @Override
    public VertexConsumer setLineWidth(float f) {
        return null;
    }

    @Override
    public VertexEmitter setNormal(PoseStack.Pose pose, float x, float y, float z) {
        final Matrix3f m = pose.normal();
        final float nx = x * m.m00() + y * m.m01() + z * m.m02();
        final float ny = x * m.m10() + y * m.m11() + z * m.m12();
        final float nz = x * m.m20() + y * m.m21() + z * m.m22();

        setNormalImpl(vertexIndex, nx, ny, nz);
        return this;
    }

    @Override
    public void putBulkData(PoseStack.Pose pose, BakedQuad quad, float[] brightness, float red, float green, float blue,
                            float alpha, int[] lightmap, int packedOverlay) {
        flushVertices();

        geometryInvalid = true;

        Matrix4f model = pose.pose();
        Matrix3f normal = pose.normal();
        final int a = toFixed(alpha);

        // putBulkData ignores vertex normals
        final Vec3i n = quad.direction().getUnitVec3i();
        final float nx = n.getX() * normal.m00() + n.getY() * normal.m01() + n.getZ() * normal.m02();
        final float ny = n.getX() * normal.m10() + n.getY() * normal.m11() + n.getZ() * normal.m12();
        final float nz = n.getX() * normal.m20() + n.getY() * normal.m21() + n.getZ() * normal.m22();
        final int packedNormal = NormalHelper.packNormal(nx, ny, nz);

        for (int i = 0; i < 4; i++) {
            final int index = baseIndex + format.headerStride + i * VERTEX_STRIDE;

            // transform position
            final Vector3fc position = quad.position(i);
            final float opx = position.x();
            final float opy = position.y();
            final float opz = position.z();
            final float px = opx * model.m00() + opy * model.m01() + opz * model.m02() + model.m03();
            final float py = opx * model.m10() + opy * model.m11() + opz * model.m12() + model.m13();
            final float pz = opx * model.m20() + opy * model.m21() + opz * model.m22() + model.m23();
            data[index + VERTEX_X] = Float.floatToRawIntBits(px);
            data[index + VERTEX_Y] = Float.floatToRawIntBits(py);
            data[index + VERTEX_Z] = Float.floatToRawIntBits(pz);

            // apply the uv
            long packedUv = quad.packedUV(i);
            data[index + VERTEX_U] = Float.floatToRawIntBits(UVPair.unpackU(packedUv));
            data[index + VERTEX_V] = Float.floatToRawIntBits(UVPair.unpackV(packedUv));

            // transform color
            data[index + VERTEX_COLOR] =
                toArgb(toFixed(brightness[i] * red), toFixed(brightness[i] * green), toFixed(brightness[i] * blue),
                    a);

            // set lightmap
            data[index + VERTEX_LIGHTMAP] = lightmap[i];

            // set normal
            data[index + VERTEX_NORMAL] = packedNormal;
        }
    }

    /**
     * Ensures that the previous vertex is finished and that we are in vertex-building mode.
     */
    protected void startVertex() {
        if (buildingVertex) {
            if (vertexIndex >= 3) {
                emit();
            } else {
                vertexIndex++;
            }
        }
        buildingVertex = true;
    }

    @Override
    public PooledQuadEmitter withTransformQuad(QuadTransform<?>[] transforms, @UnknownNullability Object[] contexts) {
        return null;
    }

    @Override
    public PooledVertexEmitter withTransformVertex(QuadTransform<?>[] transforms,
                                                   @UnknownNullability Object[] contexts) {
        return null;
    }

    /**
     * Ensures that all vertices are finished and emitted.
     */
    @Override
    public void flushVertices() {
        if (buildingVertex) {
            // buildingVertex is reset by emit
            emit();
        }
    }

    @Override
    public VertexConsumer getBuffer(RenderType renderType) {
        return null;
    }
}
