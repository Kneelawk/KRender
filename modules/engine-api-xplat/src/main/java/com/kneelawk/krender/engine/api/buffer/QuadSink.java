package com.kneelawk.krender.engine.api.buffer;

import org.jetbrains.annotations.UnknownNullability;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import com.kneelawk.krender.engine.api.RendererDependent;

/**
 * Universal interface for API-users to send vertex data to a backend.
 * <p>
 * This supports both the per-quad style of the Fabric Render API and the per-vertex style of vanilla.
 */
public interface QuadSink extends MultiBufferSource, RendererDependent {
    /**
     * {@return a view of this quad sink as a per-quad model-data emitter}
     */
    QuadEmitter asQuadEmitter();

    /**
     * {@return a view of this quad sink as a per-vertex model-data emitter}
     */
    VertexEmitter asVertexEmitter();

    /**
     * Gets a view of this quad sink as a per-quad model-data emitter that transforms its quads with the given transform.
     *
     * @param context   the context to be passed to the transform.
     * @param transform the quad transform that applies to every quad.
     * @param <C>       the type of context to be passed to the transform.
     * @return the per-quad model-data emitter that transforms quads.
     */
    <C> PooledQuadEmitter withTransformQuad(@UnknownNullability C context, QuadTransform<C> transform);

    /**
     * Gets a view of this quad sink as a per-vertex model-data emitter that transforms its quads with the given transform.
     *
     * @param context   the context to be passed to the transform.
     * @param transform the quad transform that applies to every quad.
     * @param <C>       the type of context to be passed to the transform.
     * @return the per-vertex model-data emitter that transforms quads.
     */
    <C> PooledVertexEmitter withTransformVertex(@UnknownNullability C context, QuadTransform<C> transform);

    /**
     * Close this quad sink.
     * <p>
     * This only does anything on pooled emitters.
     */
    default void close() {}

    /**
     * {@return whether this quad sink transforms its quads}
     */
    default boolean isTransformer() {
        return false;
    }

    @Override
    default VertexConsumer getBuffer(RenderType renderType) {
        return asVertexEmitter().setDefaultMaterial(getRendererOrDefault().materialManager().fromVanilla(renderType));
    }

    /**
     * If this quad sink was previously used as a vertex consumer or multi-buffer source, then this flushes the last
     * vertex, emitting the final quad.
     */
    void flushVertices();
}
