package com.kneelawk.krender.engine.base.buffer;

import org.jetbrains.annotations.Nullable;

import org.joml.Vector2f;
import org.joml.Vector3f;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.buffer.QuadView;
import com.kneelawk.krender.engine.api.material.RenderMaterial;

import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.HEADER_BITS;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.HEADER_STRIDE;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_COLOR;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_LIGHTMAP;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_STRIDE;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_U;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_V;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_X;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_Y;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.VERTEX_Z;
import static com.kneelawk.krender.engine.base.buffer.BaseQuadFormat.isNormalPresent;

// This class is largely based on the Fabric Render API's QuadViewImpl.

/**
 * Base {@link QuadView} implementation for use in backends.
 */
public class BaseQuadView implements QuadView {
    /**
     * The renderer associated with this quad view.
     */
    protected final @Nullable KRenderer renderer;

    /**
     * Header and quad data.
     */
    protected int[] data;

    /**
     * The index in the data array where this view's data actually starts.
     */
    protected int baseIndex = 0;

    public BaseQuadView(@Nullable KRenderer renderer) {this.renderer = renderer;}

    @Override
    public void copyTo(QuadEmitter target) {
        // TODO
    }

    @Override
    public float getX(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_X]);
    }

    @Override
    public float getY(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_Y]);
    }

    @Override
    public float getZ(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_Z]);
    }

    @Override
    public float getPosByIndex(int vertexIndex, int coordinateIndex) {
        return Float.intBitsToFloat(data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_X + coordinateIndex]);
    }

    @Override
    public Vector3f copyPos(int vertexIndex, @Nullable Vector3f target) {
        if (target == null) {
            target = new Vector3f();
        }

        final int index = baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_X;
        target.set(Float.intBitsToFloat(data[index]), Float.intBitsToFloat(data[index + 1]),
            Float.intBitsToFloat(data[index + 2]));
        return target;
    }

    @Override
    public int getColor(int vertexIndex) {
        return data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_COLOR];
    }

    @Override
    public float getU(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_U]);
    }

    @Override
    public float getV(int vertexIndex) {
        return Float.intBitsToFloat(data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_V]);
    }

    @Override
    public float getUvByIndex(int vertexIndex, int coordinateIndex) {
        return Float.intBitsToFloat(data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_U + coordinateIndex]);
    }

    @Override
    public Vector2f copyUv(int vertexIndex, @Nullable Vector2f target) {
        if (target == null) {
            target = new Vector2f();
        }

        final int index = baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_U;
        target.set(Float.intBitsToFloat(data[index]), Float.intBitsToFloat(data[index + 1]));
        return target;
    }

    @Override
    public int getLightmap(int vertexIndex) {
        return data[baseIndex + HEADER_STRIDE + vertexIndex * VERTEX_STRIDE + VERTEX_LIGHTMAP];
    }

    @Override
    public boolean hasNormal(int vertexIndex) {
        return isNormalPresent(data[baseIndex + HEADER_BITS], vertexIndex);
    }

    @Override
    public float getNormalX(int vertexIndex) {
        return 0;
    }

    @Override
    public float getNormalY(int vertexIndex) {
        return 0;
    }

    @Override
    public float getNormalZ(int vertexIndex) {
        return 0;
    }

    @Override
    public float getNormalByIndex(int vertexIndex, int coordinateIndex) {
        return 0;
    }

    @Override
    public @Nullable Vector3f copyNormal(int vertexIndex, @Nullable Vector3f target) {
        return null;
    }

    @Override
    public @Nullable Direction getCullFace() {
        return null;
    }

    @Override
    public Direction getLightFace() {
        return null;
    }

    @Override
    public @Nullable Direction getNominalFace() {
        return null;
    }

    @Override
    public Vector3f getFaceNormal() {
        return null;
    }

    @Override
    public RenderMaterial getMaterial() {
        return null;
    }

    @Override
    public int getColorIndex() {
        return 0;
    }

    @Override
    public int getTag() {
        return 0;
    }

    @Override
    public void toVanilla(int[] target, int targetIndex) {

    }

    @Override
    public BakedQuad toBakedQuad(TextureAtlasSprite sprite) {
        return null;
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return renderer;
    }
}
