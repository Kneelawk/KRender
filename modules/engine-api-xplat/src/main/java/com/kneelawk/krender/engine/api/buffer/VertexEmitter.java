package com.kneelawk.krender.engine.api.buffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.kneelawk.krender.engine.api.material.RenderMaterial;

/**
 * Per-vertex vanilla style model emitter.
 */
public interface VertexEmitter extends VertexConsumer, QuadSink {
    /**
     * Sets the default material for this vertex emitter.
     *
     * @param material the render material to make default.
     * @return this vertex emitter.
     */
    VertexEmitter setDefaultMaterial(RenderMaterial material);

    /**
     * Sets the current material.
     * <p>
     * This material persists between vertices until a new material is set.
     *
     * @param material the material to set.
     * @return this vertex emitter.
     */
    VertexEmitter setMaterial(RenderMaterial material);

    @Override
    VertexEmitter addVertex(float x, float y, float z);

    @Override
    VertexEmitter setColor(int red, int green, int blue, int alpha);

    @Override
    VertexEmitter setUv(float u, float v);

    @Override
    VertexEmitter setUv1(int u, int v);

    @Override
    VertexEmitter setUv2(int u, int v);

    @Override
    VertexEmitter setNormal(float normalX, float normalY, float normalZ);

    @Override
    default VertexEmitter setColor(float red, float green, float blue, float alpha) {
        VertexConsumer.super.setColor(red, green, blue, alpha);
        return this;
    }

    @Override
    default VertexEmitter setColor(int color) {
        VertexConsumer.super.setColor(color);
        return this;
    }

    @Override
    default VertexEmitter setWhiteAlpha(int alpha) {
        VertexConsumer.super.setWhiteAlpha(alpha);
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
    default VertexEmitter addVertex(Vector3f pos) {
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
    default VertexEmitter addVertex(Matrix4f pose, float x, float y, float z) {
        VertexConsumer.super.addVertex(pose, x, y, z);
        return this;
    }

    @Override
    default VertexEmitter setNormal(PoseStack.Pose pose, float normalX, float normalY, float normalZ) {
        VertexConsumer.super.setNormal(pose, normalX, normalY, normalZ);
        return this;
    }

    @Override
    default VertexEmitter asVertexEmitter() {
        return this;
    }
}
