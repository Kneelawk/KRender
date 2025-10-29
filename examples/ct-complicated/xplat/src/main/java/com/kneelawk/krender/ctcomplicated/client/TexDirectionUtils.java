package com.kneelawk.krender.ctcomplicated.client;

import java.util.Arrays;

import net.minecraft.core.Direction;

/**
 * Utilities for getting relative texture directions.
 */
public class TexDirectionUtils {
    private static final Direction[] UPS = {
        Direction.SOUTH,
        Direction.NORTH,
        Direction.UP,
        Direction.UP,
        Direction.UP,
        Direction.UP
    };

    private static final Direction[] DOWNS = Arrays.stream(UPS).map(Direction::getOpposite).toArray(Direction[]::new);

    private static final Direction[] RIGHTS = {
        Direction.EAST,
        Direction.EAST,
        Direction.WEST,
        Direction.EAST,
        Direction.SOUTH,
        Direction.NORTH
    };

    private static final Direction[] LEFTS =
        Arrays.stream(RIGHTS).map(Direction::getOpposite).toArray(Direction[]::new);

    /**
     * Gets the direction in a texture's up direction.
     *
     * @param normal the texture normal.
     * @return the texture's up direction.
     */
    public static Direction texUp(Direction normal) {
        return UPS[normal.get3DDataValue()];
    }

    /**
     * Gets the direction in a texture's down direction.
     *
     * @param normal the texture normal.
     * @return the texture's down direction.
     */
    public static Direction texDown(Direction normal) {
        return DOWNS[normal.get3DDataValue()];
    }

    /**
     * Gets the direction in the texture's right direction.
     *
     * @param normal the texture normal.
     * @return the texture's right direction.
     */
    public static Direction texRight(Direction normal) {
        return RIGHTS[normal.get3DDataValue()];
    }

    /**
     * Gets the direction in the texture's left direction.
     *
     * @param normal the texture normal.
     * @return the texture's left direction.
     */
    public static Direction texLeft(Direction normal) {
        return LEFTS[normal.get3DDataValue()];
    }
}
