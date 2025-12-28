package com.kneelawk.krender.engine.base.buffer;

import org.jspecify.annotations.Nullable;

import org.joml.Vector2f;
import org.joml.Vector3f;

import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.TriState;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.buffer.QuadView;

import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_COLOR;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_LIGHTMAP;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_NORMAL;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_STRIDE;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_U;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_V;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_X;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_Y;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_Z;

// This class is largely based on the Fabric Render API's QuadViewImpl.

/**
 * Base {@link QuadView} implementation for use in backends.
 */
public class BaseQuadView implements QuadView {
    /**
     * The renderer associated with this quad view.
     */
    protected final KRenderer renderer;

    /**
     * The quad format for this quad view's renderer.
     */
    protected final BaseQuadFormat format;

    /**
     * Header and quad data.
     */
    protected int[] data;

    /**
     * The index in the data array where this view's data actually starts.
     */
    protected int baseIndex = 0;

    /**
     * The current nominal face of this quad.
     */
    protected @Nullable Direction nominalFace;
    /**
     * Whether the current quad's geometry bits are incorrect.
     */
    protected boolean geometryInvalid = true;
    /**
     * The calculated, cached face normal.
     */
    protected final Vector3f faceNormal = new Vector3f();

    /**
     * Vec3 scratch vector for copying.
     */
    protected final Vector3f scratch3 = new Vector3f();
    /**
     * Vec2 scratch vector for copying.
     */
    protected final Vector2f scratch2 = new Vector2f();

    /**
     * Creates a new base quad view using the given KRenderer for things like material lookup.
     *
     * @param renderer the KRenderer to associate this quad view with.
     */
    public BaseQuadView(KRenderer renderer) {
        this.renderer = renderer;
        this.format = BaseQuadFormat.get(renderer);
    }

    /**
     * Loads the given data buffer into this quad view.
     * <p>
     * This assumes that the data buffer contains valid geometry flags and that they do not need to be recalculated.
     *
     * @param data      the new data buffer.
     * @param baseIndex the index of the first int of the quad in the buffer.
     */
    public void load(int[] data, int baseIndex) {
        this.data = data;
        this.baseIndex = baseIndex;
        load();
    }

    /**
     * Loads the cache from the data buffer.
     */
    public void load() {
        geometryInvalid = false;
        nominalFace = getLightFace();
        NormalHelper.unpackNormal(getPackedFaceNormal(), faceNormal);
    }

    /**
     * Calculates the cache based on existing data and writes it to the data buffer.
     */
    protected void computeGeometry() {
        if (geometryInvalid) {
            geometryInvalid = false;

            NormalHelper.computeFaceNormal(faceNormal, this);
            data[baseIndex + format.headerFaceNormal] = NormalHelper.packNormal(faceNormal);

            format.light.setI(data, baseIndex + format.headerBits, GeometryHelper.computeLightFace(this));

            format.geometry.setI(data, baseIndex + format.headerBits, GeometryHelper.computeGeometryFlags(this));
        }
    }

    @Override
    public void copyTo(QuadEmitter target) {
        computeGeometry();

        if (target instanceof BaseQuadEmitter quad) {
            System.arraycopy(data, baseIndex, quad.data, quad.baseIndex, format.totalStride);
            quad.faceNormal.set(faceNormal);
            quad.nominalFace = nominalFace;
            quad.geometryInvalid = false;
        } else {
            Vector3f vec3 = scratch3;
            Vector2f vec2 = scratch2;

            target.setCullFace(getCullFace());
            target.setNominalFace(getNominalFace());
            target.setRenderLayer(getRenderLayer());
            target.setEmissive(isEmissive());
            target.setDiffuseDisabled(isDiffuseDisabled());
            target.setAmbientOcclusionMode(getAmbientOcclusionMode());
            target.setFoilType(getFoilType());
            target.setTexture(target.getRendererOrDefault().converter().toAssociated(getTexture()));
            target.setTintIndex(getTintIndex());
            target.setTag(getTag());

            for (int i = 0; i < 4; i++) {
                copyPos(i, vec3);
                target.setPos(i, vec3);

                target.setColor(i, getColor(i));

                copyUv(i, vec2);
                target.setUv(i, vec2);

                target.setLightmap(i, getLightmap(i));

                if (hasNormal(i)) {
                    copyNormal(i, vec3);
                    target.setNormal(i, vec3);
                } else {
                    target.removeNormal(i);
                }
            }
        }
    }

    @Override
    public float getX(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_X]);
    }

    @Override
    public float getY(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_Y]);
    }

    @Override
    public float getZ(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_Z]);
    }

    @Override
    public float getPosByIndex(int vertexIndex, int coordinateIndex) {
        return Float.intBitsToFloat(
            data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_X + coordinateIndex]);
    }

    @Override
    public Vector3f copyPos(int vertexIndex, @Nullable Vector3f target) {
        if (target == null) {
            target = new Vector3f();
        }

        final int index = baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_X;
        target.set(Float.intBitsToFloat(data[index]), Float.intBitsToFloat(data[index + 1]),
            Float.intBitsToFloat(data[index + 2]));
        return target;
    }

    @Override
    public int getColor(int vertexIndex) {
        return data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_COLOR];
    }

    @Override
    public float getU(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_U]);
    }

    @Override
    public float getV(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_V]);
    }

    @Override
    public float getUvByIndex(int vertexIndex, int coordinateIndex) {
        return Float.intBitsToFloat(
            data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_U + coordinateIndex]);
    }

    @Override
    public Vector2f copyUv(int vertexIndex, @Nullable Vector2f target) {
        if (target == null) {
            target = new Vector2f();
        }

        final int index = baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_U;
        target.set(Float.intBitsToFloat(data[index]), Float.intBitsToFloat(data[index + 1]));
        return target;
    }

    @Override
    public int getLightmap(int vertexIndex) {
        return data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_LIGHTMAP];
    }

    @Override
    public boolean hasNormal(int vertexIndex) {
        return format.normals[vertexIndex].getI(data, baseIndex + format.headerBits);
    }

    @Override
    public float getNormalX(int vertexIndex) {
        return hasNormal(vertexIndex) ?
            NormalHelper.unpackNormalX(
                data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL]) :
            Float.NaN;
    }

    @Override
    public float getNormalY(int vertexIndex) {
        return hasNormal(vertexIndex) ?
            NormalHelper.unpackNormalY(
                data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL]) :
            Float.NaN;
    }

    @Override
    public float getNormalZ(int vertexIndex) {
        return hasNormal(vertexIndex) ?
            NormalHelper.unpackNormalZ(
                data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL]) :
            Float.NaN;
    }

    @Override
    public float getNormalByIndex(int vertexIndex, int coordinateIndex) {
        return hasNormal(vertexIndex) ?
            NormalHelper.unpackNormal(
                data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL],
                coordinateIndex) : Float.NaN;
    }

    @Override
    public @Nullable Vector3f copyNormal(int vertexIndex, @Nullable Vector3f target) {
        if (!hasNormal(vertexIndex)) return null;

        if (target == null) {
            target = new Vector3f();
        }

        NormalHelper.unpackNormal(data[baseIndex + format.headerStride + vertexIndex * VERTEX_STRIDE + VERTEX_NORMAL],
            target);

        return target;
    }

    @Override
    public @Nullable Direction getCullFace() {
        return format.cull.getI(data, baseIndex + format.headerBits);
    }

    @Override
    public Direction getLightFace() {
        computeGeometry();
        return format.light.getI(data, baseIndex + format.headerBits);
    }

    @Override
    public @Nullable Direction getNominalFace() {
        return nominalFace;
    }

    @Override
    public Vector3f getFaceNormal() {
        computeGeometry();
        return faceNormal;
    }

    /**
     * {@return the packed face normal of this quad}
     */
    public int getPackedFaceNormal() {
        computeGeometry();
        return data[baseIndex + format.headerFaceNormal];
    }

    @Override
    public @Nullable ChunkSectionLayer getRenderLayer() {
        return format.renderLayer.getI(data, baseIndex + format.headerBits);
    }

    @Override
    public boolean isEmissive() {
        return format.emissive.getI(data, baseIndex + format.headerBits);
    }

    @Override
    public boolean isDiffuseDisabled() {
        return format.diffuseDisabled.getI(data, baseIndex + format.headerBits);
    }

    @Override
    public TriState getAmbientOcclusionMode() {
        return format.ambientOcclusion.getI(data, baseIndex + format.headerBits);
    }

    @Override
    public ItemStackRenderState.@Nullable FoilType getFoilType() {
        return format.foilType.getI(data, baseIndex + format.headerBits);
    }

    @Override
    public int getTextureIntId() {
        return format.texture.getI(data, baseIndex + format.headerBits);
    }

    @Override
    public int getTintIndex() {
        return data[baseIndex + format.headerTintIndex];
    }

    @Override
    public int getTag() {
        return data[baseIndex + format.headerTag];
    }

    @Override
    public BakedQuad toBakedQuad(TextureAtlasSprite sprite) {
        boolean shade = !isDiffuseDisabled();
        int emission = isEmissive() ? 15 : 0;
        return new BakedQuad(copyPos(0, null), copyPos(1, null), copyPos(2, null), copyPos(3, null),
            UVPair.pack(getU(0), getV(0)), UVPair.pack(getU(1), getV(1)), UVPair.pack(getU(2), getV(2)),
            UVPair.pack(getU(3), getV(3)), getTintIndex(), getLightFace(), sprite, shade, emission);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return renderer;
    }
}
