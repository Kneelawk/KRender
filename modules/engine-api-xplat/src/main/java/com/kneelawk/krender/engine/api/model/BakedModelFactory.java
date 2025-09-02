package com.kneelawk.krender.engine.api.model;

import com.kneelawk.krender.engine.api.RendererDependent;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;

import net.minecraft.client.renderer.block.model.BlockStateModel;

/**
 * Responsible for wrapping a {@link BlockStateModelCore} and creating a {@link net.minecraft.client.renderer.block.model.BlockStateModel}.
 */
public interface BakedModelFactory extends RendererDependent {
    /**
     * Sets whether this baked model factory creates baked models that cache their quads.
     * <p>
     * Note: if caching is disabled, then {@link BlockStateModelCore#renderBlock(QuadEmitter, Object)} may be called several
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
     * Creates a {@link net.minecraft.client.renderer.block.model.BlockStateModel} by wrapping a {@link BlockStateModelCore} in a platform-dependent {@link BlockStateModel}
     * implementation, allowing for better integration with NeoForge and FRAPI's own interfaces.
     *
     * @param core the core to wrap.
     * @return the created baked model.
     */
    BlockStateModel wrap(BlockStateModelCore<?> core);
}
