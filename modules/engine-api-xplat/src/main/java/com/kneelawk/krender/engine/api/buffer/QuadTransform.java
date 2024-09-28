package com.kneelawk.krender.engine.api.buffer;

import org.jetbrains.annotations.UnknownNullability;

/**
 * Used to transform quads as they are being emitted.
 *
 * @param <C> the type of context this quad transform requires.
 */
@FunctionalInterface
public interface QuadTransform<C> {
    /**
     * Copy a quad from the quad-view input to the quad-emitter output, transforming it along the way.
     * <p>
     * If this transform wishes to drop a quad, it simply should not copy it to the output quad emitter.
     * <p>
     * {@link QuadEmitter#emit()} must be called after every quad has been copied, otherwise the quad will be dropped.
     *
     * @param context the transform's context.
     * @param input   the quad view to copy the quad from.
     * @param output  the quad emitter to copy the quad to.
     */
    void transform(@UnknownNullability C context, QuadView input, QuadEmitter output);
}
