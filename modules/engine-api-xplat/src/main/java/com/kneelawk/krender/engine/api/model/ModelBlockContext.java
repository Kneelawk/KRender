package com.kneelawk.krender.engine.api.model;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Holds access to all available context when preparing to render a model.
 */
public interface ModelBlockContext {
    /**
     * {@return the level view for this chunk building thread}
     */
    BlockAndTintGetter getLevel();

    /**
     * {@return the position of the block being rendered}
     */
    BlockPos getPos();

    /**
     * {@return the blockstate of the block being rendered}
     */
    BlockState getState();

    /**
     * {@return a random for this block position and state}
     */
    RandomSource getRandom();
}
