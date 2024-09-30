package com.kneelawk.krender.engine.base.model;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import com.kneelawk.krender.engine.api.model.ModelBlockContext;

/**
 * Base implementation of a {@link ModelBlockContext} for use in rendering backends.
 * <p>
 * The data contained may change, but changing or removing a constructor is considered an API-breaking change.
 */
public class BaseModelBlockContext implements ModelBlockContext {
    /**
     * The level access.
     */
    protected final BlockAndTintGetter level;
    /**
     * The position of the block being rendered.
     */
    protected final BlockPos pos;
    /**
     * The state of the block being rendered.
     */
    protected final BlockState state;
    /**
     * The random for the given block state and position.
     */
    protected final RandomSource random;

    /**
     * Create a new base block model context.
     *
     * @param level  the level access.
     * @param pos    the position of the block being rendered.
     * @param state  the state of the block being rendered.
     * @param random the random for the given block state and position.
     */
    public BaseModelBlockContext(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        this.level = level;
        this.pos = pos;
        this.state = state;
        this.random = random;
    }

    @Override
    public BlockAndTintGetter getLevel() {
        return level;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public BlockState getState() {
        return state;
    }

    @Override
    public RandomSource getRandom() {
        return random;
    }
}
