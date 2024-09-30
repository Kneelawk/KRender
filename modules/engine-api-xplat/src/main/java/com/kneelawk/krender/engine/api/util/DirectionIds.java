package com.kneelawk.krender.engine.api.util;

import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

/**
 * Utility for converting {@link Direction}s into integer ids.
 */
public final class DirectionIds {
    private DirectionIds() {}

    /**
     * Down direction id.
     */
    public static final int DOWN = Direction.DOWN.get3DDataValue();
    /**
     * Up direction id.
     */
    public static final int UP = Direction.UP.get3DDataValue();
    /**
     * North direction id.
     */
    public static final int NORTH = Direction.NORTH.get3DDataValue();
    /**
     * South direction id.
     */
    public static final int SOUTH = Direction.SOUTH.get3DDataValue();
    /**
     * West direction id.
     */
    public static final int WEST = Direction.WEST.get3DDataValue();
    /**
     * East direction id.
     */
    public static final int EAST = Direction.EAST.get3DDataValue();
    /**
     * Null direction id.
     */
    public static final int NULL = 6;

    /**
     * The number of directions possible.
     */
    public static final int DIRECTION_COUNT = NULL + 1;

    /**
     * The bit mask for a direction id.
     */
    public static final int DIRECTION_MASK = Mth.smallestEncompassingPowerOfTwo(NULL) - 1;
    /**
     * The number of bits in a direction id.
     */
    public static final int DIRECTION_BIT_COUNT = Integer.bitCount(DIRECTION_MASK);

    private static final Direction[] BY_ID = Arrays.copyOf(Direction.values(), 7);

    /**
     * Gets the direction id for the given direction.
     *
     * @param dir the nullable direction to get the id of.
     * @return the id for the given direction.
     */
    public static int directionToId(@Nullable Direction dir) {
        return dir == null ? NULL : dir.get3DDataValue();
    }

    /**
     * Gets the direction for the given id.
     *
     * @param id the id to get the direction of.
     * @return the direction of the given id.
     */
    public static @Nullable Direction idToDirection(int id) {
        return BY_ID[id];
    }
}
