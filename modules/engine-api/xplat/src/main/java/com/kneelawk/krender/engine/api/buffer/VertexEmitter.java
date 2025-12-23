package com.kneelawk.krender.engine.api.buffer;

import org.jspecify.annotations.Nullable;

import org.joml.Matrix3x2fc;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;

import com.kneelawk.krender.engine.api.texture.MaterialTexture;
import com.kneelawk.krender.engine.api.texture.MaterialTextureManager;
import com.kneelawk.krender.engine.api.util.TriState;

/**
 * Per-vertex vanilla style model emitter.
 */
public interface VertexEmitter extends VertexConsumer, QuadSink {
    /**
     * Sets this quad's blend mode.
     * <p>
     * This property persists between vertices until a new one is set.
     *
     * @param renderLayer the new blend mode.
     * @return this quad emitter.
     */
    VertexEmitter setRenderLayer(@Nullable ChunkSectionLayer renderLayer);

    /**
     * Sets whether this quad is emissive. This causes it to ignore lighting values.
     * <p>
     * This property persists between vertices until a new one is set.
     *
     * @param emissive the emissive value.
     * @return this quad emitter.
     */
    VertexEmitter setEmissive(boolean emissive);

    /**
     * Sets whether diffuse shading is disabled.
     * <p>
     * This property persists between vertices until a new one is set.
     *
     * @param disabled whether to disable diffuse shading.
     * @return this quad emitter.
     */
    VertexEmitter setDiffuseDisabled(boolean disabled);

    /**
     * Sets whether ambient occlusion is force enabled, disabled, or left up to the model.
     * <p>
     * Note: not all backends may respect this value, or some may only respect a {@code FALSE} value, treating a
     * {@code TRUE} value the same as a {@code DEFAULT} value.
     * <p>
     * This property persists between vertices until a new one is set.
     *
     * @param mode whether ambient occlusion is force enabled, disabled, or left up to the model.
     * @return this quad emitter.
     */
    VertexEmitter setAmbientOcclusionMode(TriState mode);

    /**
     * Sets the kind of foil used by this quad.
     * <p>
     * This is usually not supported on terrain rendering on most backends.
     * <p>
     * This property persists between vertices until a new one is set.
     *
     * @param foilType the kind of foil to be used by this quad.
     * @return this quad emitter.
     */
    VertexEmitter setFoilType(ItemStackRenderState.@Nullable FoilType foilType);

    /**
     * Sets the texture or texture atlas to be used on quads rendered with this quad, looking up by integer id.
     * <p>
     * When rendering terrain, most backends only support the {@link MaterialTextureManager#blockAtlas} or {@link MaterialTextureManager#itemAtlas} textures.
     * <p>
     * This property persists between vertices until a new one is set.
     *
     * @param textureIntId the integer id of the texture to be associated with this quad.
     * @return this quad emitter.
     */
    VertexEmitter setTextureIntId(int textureIntId);

    /**
     * Sets the texture or texture atlas to be used on quads rendered with this quad.
     * <p>
     * When rendering terrain, most backends only support the {@link MaterialTextureManager#blockAtlas()} texture.
     * <p>
     * This property persists between vertices until a new one is set.
     *
     * @param texture the texture to be associated with this quad.
     * @return this quad emitter.
     */
    default VertexEmitter setTexture(MaterialTexture texture) {
        return setTextureIntId(texture.intId());
    }

    @Override
    VertexEmitter addVertex(float x, float y, float z);

    @Override
    VertexEmitter setColor(int red, int green, int blue, int alpha);

    @Override
    VertexEmitter setColor(int color);

    @Override
    VertexEmitter setUv(float u, float v);

    @Override
    VertexEmitter setUv1(int u, int v);

    @Override
    VertexEmitter setUv2(int u, int v);

    @Override
    VertexEmitter setNormal(float normalX, float normalY, float normalZ);

    @Override
    VertexConsumer setLineWidth(float f);

    @Override
    default VertexEmitter setColor(float red, float green, float blue, float alpha) {
        VertexConsumer.super.setColor(red, green, blue, alpha);
        return this;
    }

    @Override
    default VertexEmitter setLight(int packedLight) {
        VertexConsumer.super.setLight(packedLight);
        return this;
    }

    @Override
    default VertexEmitter setOverlay(int packedOverlay) {
        VertexConsumer.super.setOverlay(packedOverlay);
        return this;
    }

    @Override
    default VertexEmitter addVertex(Vector3fc pos) {
        VertexConsumer.super.addVertex(pos);
        return this;
    }

    @Override
    default VertexEmitter addVertex(PoseStack.Pose pose, Vector3f pos) {
        VertexConsumer.super.addVertex(pose, pos);
        return this;
    }

    @Override
    default VertexEmitter addVertex(PoseStack.Pose pose, float x, float y, float z) {
        VertexConsumer.super.addVertex(pose, x, y, z);
        return this;
    }

    @Override
    default VertexEmitter addVertex(Matrix4fc pose, float x, float y, float z) {
        VertexConsumer.super.addVertex(pose, x, y, z);
        return this;
    }

    @Override
    default VertexEmitter addVertexWith2DPose(Matrix3x2fc $$0, float $$1, float $$2) {
        VertexConsumer.super.addVertexWith2DPose($$0, $$1, $$2);
        return this;
    }

    @Override
    default VertexEmitter setNormal(PoseStack.Pose pose, float normalX, float normalY, float normalZ) {
        VertexConsumer.super.setNormal(pose, normalX, normalY, normalZ);
        return this;
    }

    @Override
    default VertexEmitter setNormal(PoseStack.Pose $$0, Vector3f $$1) {
        VertexConsumer.super.setNormal($$0, $$1);
        return this;
    }

    @Override
    default VertexEmitter asVertexEmitter() {
        return this;
    }
}
