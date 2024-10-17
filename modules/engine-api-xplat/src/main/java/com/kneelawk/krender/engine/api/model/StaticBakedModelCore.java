package com.kneelawk.krender.engine.api.model;

import org.jetbrains.annotations.UnknownNullability;

import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.mesh.Mesh;

/**
 * Implemented by {@link BakedModelCore}s that output the same mesh data independent of context.
 */
public interface StaticBakedModelCore extends BakedModelCore<Void> {
    /**
     * {@return this model's static mesh}
     */
    Mesh getMesh();

    @Override
    @UnknownNullability
    default Void getBlockKey(ModelBlockContext ctx) {
        return null;
    }

    @Override
    default void renderBlock(QuadEmitter renderTo, @UnknownNullability Void blockKey) {
        getMesh().outputTo(renderTo);
    }

    @Override
    default void renderItem(QuadEmitter renderTo, ModelItemContext ctx) {
        getMesh().outputTo(renderTo);
    }
}
