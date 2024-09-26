package com.kneelawk.krender.engine.base.buffer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.buffer.VertexEmitter;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.api.util.ColorUtil;
import com.kneelawk.krender.engine.base.BaseKRendererApi;
import com.kneelawk.krender.engine.base.material.BaseMaterialViewApi;

import static com.kneelawk.krender.engine.api.util.ColorUtil.blue;
import static com.kneelawk.krender.engine.api.util.ColorUtil.green;
import static com.kneelawk.krender.engine.api.util.ColorUtil.red;
import static com.kneelawk.krender.engine.api.util.ColorUtil.scale;
import static com.kneelawk.krender.engine.api.util.ColorUtil.toArgb;
import static com.kneelawk.krender.engine.api.util.ColorUtil.toFixed;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.EMPTY;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.HEADER_BITS;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.HEADER_COLOR_INDEX;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.HEADER_STRIDE;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.HEADER_TAG;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.TOTAL_STRIDE;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_COLOR;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_LIGHTMAP;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_NORMAL;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_STRIDE;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_U;
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

    protected RenderMaterial defaultMaterial = renderer.materialManager().defaultMaterial();

    /**
     * Creates a new base quad emitter associated with the given KRenderer.
     *
     * @param renderer the KRenderer to associate this quad emitter with.
     */
    public BaseQuadEmitter(BaseKRendererApi renderer) {
        super(renderer);
    }

    /**
     * Resets the quad data.
     */
    public void clear() {
        System.arraycopy(EMPTY, 0, data, baseIndex, TOTAL_STRIDE);
        geometryInvalid = true;
        nominalFace = null;
        setColorIndex(-1);
        setCullFace(null);
        setMaterial(defaultMaterial);
        vertexIndex = 0;
        buildingVertex = false;
    }

    /**
     * Call before emitting.
     */
    public void complete() {
        computeGeometry();
        vertexIndex = 0;
    }

    @Override
    public QuadEmitter setPos(int vertexIndex, float x, float y, float z) {
        finishVertices();
        return setPosImpl(vertexIndex, x, y, z);
    }

    private @NotNull BaseQuadEmitter setPosImpl(int vertexIndex, float x, float y, float z) {
        final int index = baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_X;
        data[index] = Float.floatToRawIntBits(x);
        data[index + 1] = Float.floatToRawIntBits(y);
        data[index + 2] = Float.floatToRawIntBits(z);
        geometryInvalid = true;
        return this;
    }

    @Override
    public QuadEmitter setPosByIndex(int vertexIndex, int coordinateIndex, float value) {
        finishVertices();
        data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_X + coordinateIndex] =
            Float.floatToRawIntBits(value);
        geometryInvalid = true;
        return this;
    }

    @Override
    public QuadEmitter setColor(int vertexIndex, int color) {
        finishVertices();
        return setColorImpl(vertexIndex, color);
    }

    private @NotNull BaseQuadEmitter setColorImpl(int vertexIndex, int color) {
        data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_COLOR] = color;
        return this;
    }

    @Override
    public QuadEmitter setUv(int vertexIndex, float u, float v) {
        finishVertices();
        return setUvImpl(vertexIndex, u, v);
    }

    private @NotNull BaseQuadEmitter setUvImpl(int vertexIndex, float u, float v) {
        final int index = baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_U;
        data[index] = Float.floatToRawIntBits(u);
        data[index + 1] = Float.floatToRawIntBits(v);
        return this;
    }

    @Override
    public QuadEmitter setUvByIndex(int vertexIndex, int coordinateIndex, float value) {
        finishVertices();
        data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_U + coordinateIndex] =
            Float.floatToRawIntBits(value);
        return this;
    }

    @Override
    public QuadEmitter spriteBake(TextureAtlasSprite sprite, int bakeFlags) {
        finishVertices();
        // TODO
        return this;
    }

    @Override
    public QuadEmitter setLightmap(int vertexIndex, int lightmap) {
        finishVertices();
        return setLightmapImpl(vertexIndex, lightmap);
    }

    private @NotNull BaseQuadEmitter setLightmapImpl(int vertexIndex, int lightmap) {
        data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_LIGHTMAP] = lightmap;
        return this;
    }

    @Override
    public QuadEmitter setNormal(int vertexIndex, float x, float y, float z) {
        finishVertices();
        return setNormalImpl(vertexIndex, x, y, z);
    }

    private @NotNull BaseQuadEmitter setNormalImpl(int vertexIndex, float x, float y, float z) {
        setNormal(vertexIndex);
        data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL] =
            NormalHelper.packNormal(x, y, z);
        return this;
    }

    @Override
    public QuadEmitter setNormalByIndex(int vertexIndex, int coordinateIndex, float value) {
        finishVertices();
        setNormal(vertexIndex);
        final int index = baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL;
        data[index] = NormalHelper.packNormal(data[index], value, coordinateIndex);
        return this;
    }

    @Override
    public QuadEmitter removeNormal(int vertexIndex) {
        finishVertices();
        final int index = baseIndex + HEADER_BITS;
        data[index] = BaseQuadFormat.setNormalPresent(data[index], vertexIndex, false);
        return this;
    }

    private void setNormal(int vertexIndex) {
        final int index = baseIndex + HEADER_BITS;
        data[index] = BaseQuadFormat.setNormalPresent(data[index], vertexIndex, true);
    }

    @Override
    public QuadEmitter setCullFace(@Nullable Direction face) {
        finishVertices();
        final int index = baseIndex + HEADER_BITS;
        data[index] = BaseQuadFormat.setCullFace(data[index], face);
        setNominalFace(face);
        return this;
    }

    @Override
    public QuadEmitter setNominalFace(@Nullable Direction face) {
        finishVertices();
        nominalFace = face;
        return this;
    }

    @Override
    public QuadEmitter setColorIndex(int colorIndex) {
        finishVertices();
        data[baseIndex + HEADER_COLOR_INDEX] = colorIndex;
        return this;
    }

    @Override
    public QuadEmitter setTag(int tag) {
        finishVertices();
        data[baseIndex + HEADER_TAG] = tag;
        return this;
    }

    @Override
    public QuadEmitter fromVanilla(int[] quadData, int startIndex) {
        finishVertices();

        // KRender and vanilla have mostly compatible vertex formats
        System.arraycopy(quadData, startIndex, data, baseIndex + HEADER_STRIDE, VANILLA_QUAD_STRIDE);
        geometryInvalid = true;

        for (int i = 0; i < 4; i++) {
            final int index = baseIndex + HEADER_STRIDE + i * VERTEX_STRIDE + VERTEX_COLOR;
            data[index] = ColorUtil.fromVanilla(data[index]);
        }

        return this;
    }

    @Override
    public QuadEmitter fromVanilla(BakedQuad quad, RenderMaterial material, @Nullable Direction cullFace) {
        fromVanilla(quad.getVertices(), 0);
        data[baseIndex + HEADER_BITS] = BaseQuadFormat.setCullFace(0, cullFace);
        setNominalFace(quad.getDirection());
        setColorIndex(quad.getTintIndex());

        if (!quad.isShade()) {
            material = renderer.materialManager().materialFinder().copyFrom(material).setDiffuseDisabled(true).find();
        }

        setMaterial(material);
        setTag(0);

        return this;
    }

    @Override
    public BaseQuadEmitter setDefaultMaterial(RenderMaterial material) {
        if (!(material instanceof BaseMaterialViewApi)) throw new IllegalArgumentException(
            "BaseQuadEmitters are only compatible with BaseMaterialViewApi render material impls. " +
                "You are likely attempting to use a render material from another renderer. " +
                "Please convert the render material to one compatible with this renderer via this renderer's converter.");

        defaultMaterial = material;

        return this;
    }

    @Override
    public BaseQuadEmitter setMaterial(@Nullable RenderMaterial material) {
        if (material == null) {
            material = defaultMaterial;
        }

        if (!(material instanceof BaseMaterialViewApi baseMaterial)) throw new IllegalArgumentException(
            "BaseQuadEmitters are only compatible with BaseMaterialViewApi render material impls. " +
                "You are likely attempting to use a render material from another renderer. " +
                "Please convert the render material to one compatible with this renderer via this renderer's converter.");

        data[baseIndex + HEADER_BITS] = BaseQuadFormat.setMaterial(data[baseIndex + HEADER_BITS], baseMaterial);

        return this;
    }

    @Override
    public VertexEmitter addVertex(float x, float y, float z) {
        startVertex();
        setPosImpl(vertexIndex, x, y, z);
        return this;
    }

    @Override
    public VertexEmitter addVertex(Matrix4f pose, float x, float y, float z) {
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
                            float alpha, int[] lightmap, int packedOverlay, boolean respectExistingColors) {
        finishVertices();

        // KRender and vanilla have mostly compatible vertex formats
        System.arraycopy(quad.getVertices(), 0, data, baseIndex + HEADER_STRIDE, VANILLA_QUAD_STRIDE);
        geometryInvalid = true;

        Matrix4f model = pose.pose();
        Matrix3f normal = pose.normal();
        final int a = toFixed(alpha);

        // putBulkData ignores vertex normals
        final Vec3i n = quad.getDirection().getNormal();
        final float nx = n.getX() * normal.m00() + n.getY() * normal.m01() + n.getZ() * normal.m02();
        final float ny = n.getX() * normal.m10() + n.getY() * normal.m11() + n.getZ() * normal.m12();
        final float nz = n.getX() * normal.m20() + n.getY() * normal.m21() + n.getZ() * normal.m22();
        final int packedNormal = NormalHelper.packNormal(nx, ny, nz);

        for (int i = 0; i < 4; i++) {
            final int index = baseIndex + HEADER_STRIDE + i * VERTEX_STRIDE;

            // transform position
            final float opx = Float.intBitsToFloat(data[index + VERTEX_X]);
            final float opy = Float.intBitsToFloat(data[index + VERTEX_Y]);
            final float opz = Float.intBitsToFloat(data[index + VERTEX_Z]);
            final float px = opx * model.m00() + opy * model.m01() + opz * model.m02() + model.m03();
            final float py = opx * model.m10() + opy * model.m11() + opz * model.m12() + model.m13();
            final float pz = opx * model.m20() + opy * model.m21() + opz * model.m22() + model.m23();
            data[index + VERTEX_X] = Float.floatToRawIntBits(px);
            data[index + VERTEX_Y] = Float.floatToRawIntBits(py);
            data[index + VERTEX_Z] = Float.floatToRawIntBits(pz);

            // transform color
            if (respectExistingColors) {
                final int color = ColorUtil.fromVanilla(data[index + VERTEX_COLOR]);
                data[index + VERTEX_COLOR] = toArgb(scale(red(color), toFixed(brightness[i] * red)),
                    scale(green(color), toFixed(brightness[i] * green)),
                    scale(blue(color), toFixed(brightness[i] * blue)), a);
            } else {
                data[index + VERTEX_COLOR] =
                    toArgb(toFixed(brightness[i] * red), toFixed(brightness[i] * green), toFixed(brightness[i] * blue),
                        a);
            }

            // set lightmap
            data[index + VERTEX_LIGHTMAP] = lightmap[i];

            // set normal
            data[index + VERTEX_NORMAL] = packedNormal;
        }
    }

    /**
     * Ensures that the previous vertex is finished and that we are in vertex-building mode.
     */
    public void startVertex() {
        if (buildingVertex) {
            if (vertexIndex >= 3) {
                emit();
            } else {
                vertexIndex++;
            }
        }
        buildingVertex = true;
    }

    /**
     * Ensures that all vertices are finished and emitted.
     */
    public void finishVertices() {
        if (buildingVertex) {
            buildingVertex = false;
            emit();
        }
    }
}
