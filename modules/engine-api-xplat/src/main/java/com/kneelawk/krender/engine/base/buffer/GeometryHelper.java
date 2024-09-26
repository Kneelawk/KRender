package com.kneelawk.krender.engine.base.buffer;

// This class is largely based on Fabric Render API's GeometryHelper

import org.joml.Vector3f;

import net.minecraft.core.Direction;

import com.kneelawk.krender.engine.api.buffer.QuadView;

/**
 * Static values and helpers for geometry calculations.
 */
public final class GeometryHelper {
    private GeometryHelper() {}

    private static final float EPS_MIN = 0.0001f;
    private static final float EPS_NEG = -EPS_MIN;
    private static final float EPS_MAX = 1f - EPS_MIN;

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

    /**
     * Gets the geometry flags for the given quad, based on its light face.
     *
     * @param quad the quad to get the geometry flags for.
     * @return the geometry flags for the given quad.
     */
    public static int computeGeometryFlags(QuadView quad) {
        final Direction lightFace = quad.getLightFace();
        int bits = 0;

        if (isParallel(lightFace, quad)) {
            bits |= AXIS_ALIGNED_FLAG;

            if (isCoplanar(lightFace, quad)) {
                bits |= LIGHT_FACE_FLAG;
            }
        }

        if (isCubic(lightFace, quad)) {
            bits |= CUBIC_FLAG;
        }

        return bits;
    }

    /**
     * Gets the face that the quad is closest to being aligned with.
     *
     * @param quad the quad to find the light face of.
     * @return the vanilla-equivalent light face of the given quad.
     */
    public static Direction computeLightFace(QuadView quad) {
        final Vector3f normal = quad.getFaceNormal();
        final float ax = Math.abs(normal.x);
        final float ay = Math.abs(normal.y);
        final float az = Math.abs(normal.z);

        if (ay >= ax && ay >= az) {
            return normal.y > 0 ? Direction.UP : Direction.DOWN;
        } else if (ax > ay && ax >= az) {
            return normal.x > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return normal.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    /**
     * Checks if the quad is parallel with the given face.
     *
     * @param face the face to check if the quad is parallel with.
     * @param quad the quad to check.
     * @return whether the quad is parallel with the given face.
     */
    public static boolean isParallel(Direction face, QuadView quad) {
        final int i = face.getAxis().ordinal();
        final float f = quad.getPosByIndex(0, i);
        return equal(f, quad.getPosByIndex(1, i)) && equal(f, quad.getPosByIndex(2, i)) &&
            equal(f, quad.getPosByIndex(3, i));
    }

    /**
     * Checks if the quad is coplanar with the given face of the unit cube.
     * <p>
     * This assumes that {@link #isParallel(Direction, QuadView)} has already been checked.
     * <p>
     * Note: This considers quads outside the unit qube to be coplanar.
     *
     * @param face the face of the unit cube to check if the quad is coplanar with.
     * @param quad the quad to check.
     * @return whether the quad is coplanar with the given face of the unit cube.
     */
    public static boolean isCoplanar(Direction face, QuadView quad) {
        final float f = quad.getPosByIndex(0, face.getAxis().ordinal());
        return face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? f >= EPS_MAX : f <= EPS_MIN;
    }

    /**
     * Checks if the given quad has a vertex in each corner of the given face of the unit cube.
     *
     * @param face the face of the unit cube to see if the quad is in each corner of.
     * @param quad the quad to check.
     * @return whether the quad has a vertex in each corner of the given face of the unit cube.
     */
    public static boolean isCubic(Direction face, QuadView quad) {
        // select which axis to check
        int a, b;
        switch (face.getAxis()) {
            case X -> {
                a = 1;
                b = 2;
            }
            case Y -> {
                a = 2;
                b = 0;
            }
            case Z -> {
                a = 0;
                b = 1;
            }
            default -> {return false;}
        }

        // check that at least one vertex is outside each corner of the unit face
        int corners = 0;
        for (int i = 0; i < 4; i++) {
            float af = quad.getPosByIndex(i, a);
            float bf = quad.getPosByIndex(i, b);

            if (af <= EPS_MIN) {
                if (bf <= EPS_MIN) {
                    corners |= 1;
                } else if (bf >= EPS_MAX) {
                    corners |= 2;
                } else {
                    return false;
                }
            } else if (af >= EPS_MAX) {
                if (bf <= EPS_MIN) {
                    corners |= 4;
                } else if (bf >= EPS_MAX) {
                    corners |= 8;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        // there must be one vertex in each of the corners
        return corners == 15;
    }

    private static boolean equal(float a, float b) {
        final float diff = a - b;
        return EPS_NEG < diff && diff < EPS_MIN;
    }
}
