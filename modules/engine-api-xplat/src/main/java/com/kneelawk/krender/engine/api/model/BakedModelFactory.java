package com.kneelawk.krender.engine.api.model;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.RendererDependent;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;

/**
 * Responsible for wrapping a {@link BakedModelCore} and creating a {@link BakedModel}.
 */
public interface BakedModelFactory extends RendererDependent {
    /**
     * Sets whether this baked model factory creates baked models that cache their quads.
     * <p>
     * Note: if caching is disabled, then {@link BakedModelCore#renderBlock(QuadEmitter, Object)} may be called several
     * times per rebuild to render a single block.
     * <p>
     * This is {@code true} by default.
     *
     * @param caching whether created baked models should cache their quads.
     * @return this baked model factory.
     */
    BakedModelFactory setCaching(boolean caching);

    /**
     * {@return whether caching is currently enabled for this baked model factory}
     */
    boolean isCaching();

    /**
     * Creates a {@link BakedModel} by wrapping a {@link BakedModelCore} in a platform-dependent {@link BakedModel}
     * implementation, allowing for better integration with NeoForge and FRAPI's own interfaces.
     *
     * @param core the core to wrap.
     * @return the created baked model.
     */
    BakedModel wrap(BakedModelCore<?> core);
}
