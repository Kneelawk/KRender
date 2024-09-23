package com.kneelawk.krender.engine.base.buffer;

// This class is largely based on Fabric Render API's GeometryHelper

/**
 * Static values and helpers for geometry calculations.
 */
public final class GeometryHelper {
    private GeometryHelper() {}

    /**
     * Indicates that all four corners of this quad are at corners of the unit cube.
     */
    public static final int CUBIC_FLAG = 1;

    /**
     * Indicates that this quad is parallel to its light face.
     */
    public static final int AXIS_ALIGNED_FLAG = CUBIC_FLAG << 1;

    /**
     * Indicates that this quad is coplanar with its light face.
     */
    public static final int LIGHT_FACE_FLAG = AXIS_ALIGNED_FLAG << 1;

    /**
     * The number of bits for geometry flags.
     */
    public static final int FLAG_BIT_COUNT = 3;
}
