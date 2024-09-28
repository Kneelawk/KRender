package com.kneelawk.krender.engine.base.buffer;

import org.jetbrains.annotations.UnknownNullability;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.buffer.QuadSink;
import com.kneelawk.krender.engine.api.buffer.QuadTransform;
import com.kneelawk.krender.engine.base.BaseKRendererApi;

/**
 * Manages the stacking of {@link TransformingQuadEmitter}s that can be returned by
 * {@link QuadSink#withTransformQuad(Object, QuadTransform)} or {@link QuadSink#withTransformVertex(Object, QuadTransform)}.
 */
public class TransformStack {
    /**
     * This stack's pool of unstacked quad emitters.
     */
    protected final ObjectArrayList<TransformingQuadEmitter> pool = new ObjectArrayList<>();

    /**
     * The renderer that all quad emitters from this stack will be associated with.
     */
    protected final BaseKRendererApi renderer;

    /**
     * Creates a new transform stack.
     *
     * @param renderer the renderer that all quad emitters from this stack will be associated with.
     */
    public TransformStack(BaseKRendererApi renderer) {this.renderer = renderer;}

    /**
     * Gets or creates a new transforming quad emitter with the given context, transform, and output.
     *
     * @param context   the context supplied to the transform.
     * @param transform the quad transform itself.
     * @param output    the quad emitter to write transformed quads to.
     * @return the newly prepared transforming quad emitter.
     */
    public TransformingQuadEmitter getTransform(@UnknownNullability Object context, QuadTransform<?> transform,
                                                QuadEmitter output) {
        return (pool.isEmpty() ? new TransformingQuadEmitter(renderer, this) : pool.pop()).prepare(context, transform,
            output);
    }

    /**
     * Reclaims a stacked {@link TransformingQuadEmitter} and adds it back to the pool of unstacked emitters.
     *
     * @param emitter the emitter to unstack.
     */
    public void reclaim(TransformingQuadEmitter emitter) {
        pool.add(emitter);
    }
}
