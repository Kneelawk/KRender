package com.kneelawk.krender.engine.base.buffer;

import org.jetbrains.annotations.UnknownNullability;

import com.kneelawk.krender.engine.api.buffer.PooledQuadEmitter;
import com.kneelawk.krender.engine.api.buffer.PooledVertexEmitter;
import com.kneelawk.krender.engine.api.buffer.QuadTransform;
import com.kneelawk.krender.engine.base.BaseKRendererApi;

/**
 * A quad emitter that does not apply any transforms but that can have transforms stacked on top of it.
 */
public abstract class RootQuadEmitter extends BaseQuadEmitter {
    /**
     * This root quad emitter's transform stack that all transforming emitters come from.
     */
    protected final TransformStack transformStack;

    /**
     * Creates a new root quad emitter.
     *
     * @param renderer the renderer that this emitter and all transforming emitters stacked on top of it will be
     *                 associated with.
     */
    public RootQuadEmitter(BaseKRendererApi renderer) {
        this(renderer, new TransformStack(renderer));
    }

    /**
     * Creates a new root quad emitter.
     *
     * @param renderer       the renderer that his emitter will be associated with.
     * @param transformStack the transform stack that all transforming emitters stacked on top of this one will come from.
     */
    protected RootQuadEmitter(BaseKRendererApi renderer, TransformStack transformStack) {
        super(renderer);
        this.transformStack = transformStack;
    }

    @Override
    public <C> PooledQuadEmitter withTransformQuad(@UnknownNullability C context, QuadTransform<C> transform) {
        return transformStack.getTransform(context, transform, this);
    }

    @Override
    public <C> PooledVertexEmitter withTransformVertex(@UnknownNullability C context, QuadTransform<C> transform) {
        return transformStack.getTransform(context, transform, this);
    }
}
