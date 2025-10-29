package com.kneelawk.krender.engine.base.buffer;

// This class is based on the Fabric Render API's TextureHelper.

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import com.kneelawk.krender.engine.api.buffer.QuadEmitter;

/**
 * Helps with sprite management and baking.
 */
public final class SpriteHelper {
    private SpriteHelper() {}

    private static final VertexModifier NORMALIZER = (q, i) -> q.setUv(i, q.getU(i) / 16f, q.getV(i) / 16f);

    private static final VertexModifier[] ROTATIONS = {
        null,
        (q, i) -> q.setUv(i, q.getV(i), 1f - q.getU(i)),
        (q, i) -> q.setUv(i, 1f - q.getU(i), 1f - q.getV(i)),
        (q, i) -> q.setUv(i, 1f - q.getV(i), q.getU(i)),
    };

    private static final VertexModifier U_FLIPPER = (q, i) -> q.setUv(i, 1f - q.getU(i), q.getV(i));
    private static final VertexModifier V_FLIPPER = (q, i) -> q.setUv(i, q.getU(i), 1f - q.getV(i));

    private static final VertexModifier[] UVLOCKERS = new VertexModifier[6];

    static {
        UVLOCKERS[Direction.DOWN.get3DDataValue()] = (q, i) -> q.setUv(i, q.getX(i), 1f - q.getZ(i));
        UVLOCKERS[Direction.UP.get3DDataValue()] = (q, i) -> q.setUv(i, q.getX(i), q.getZ(i));
        UVLOCKERS[Direction.NORTH.get3DDataValue()] = (q, i) -> q.setUv(i, 1f - q.getX(i), 1f - q.getY(i));
        UVLOCKERS[Direction.SOUTH.get3DDataValue()] = (q, i) -> q.setUv(i, q.getX(i), 1f - q.getY(i));
        UVLOCKERS[Direction.WEST.get3DDataValue()] = (q, i) -> q.setUv(i, q.getZ(i), 1f - q.getY(i));
        UVLOCKERS[Direction.EAST.get3DDataValue()] = (q, i) -> q.setUv(i, 1f - q.getZ(i), 1f - q.getY(i));
    }

    /**
     * Bakes a sprite to a quad by changing the quad's existing uv values to ones interpolated by the given sprite.
     *
     * @param quad      the quad emitter to bake the sprite into.
     * @param sprite    the sprite to bake.
     * @param bakeFlags the extra flags on how the sprite is to be baked.
     */
    public static void bakeSprite(QuadEmitter quad, TextureAtlasSprite sprite, int bakeFlags) {
        Direction nominalFace = quad.getNominalFace();
        if (nominalFace != null && (QuadEmitter.BAKE_LOCK_UV & bakeFlags) != 0) {
            applyModifier(quad, UVLOCKERS[nominalFace.get3DDataValue()]);
        } else if ((QuadEmitter.BAKE_DENORMALIZED & bakeFlags) != 0) {
            // we apply the normalizer first because everything else expects normalized (0.0-1.0) coordinates
            applyModifier(quad, NORMALIZER);
        }

        final int rotation = bakeFlags & 3;

        if (rotation != 0) {
            applyModifier(quad, ROTATIONS[rotation]);
        }

        if ((QuadEmitter.BAKE_FLIP_U & bakeFlags) != 0) {
            applyModifier(quad, U_FLIPPER);
        }

        if ((QuadEmitter.BAKE_FLIP_V & bakeFlags) != 0) {
            applyModifier(quad, V_FLIPPER);
        }

        interpolate(quad, sprite);
    }

    /**
     * Translates a quad's texture coordinates from full coordinates to atlas coordinates.
     *
     * @param quad   the quad emitter to interpolate the texture coordinates of.
     * @param sprite the sprite in the atlas with the coordinates that the quad is interpolated between.
     */
    public static void interpolate(QuadEmitter quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            // sprites now expect normalized coordinates where they used to expect denormalized coordinates,
            // meaning that we can now use their getU/getV methods directly without having to multiply by 16.0 first
            quad.setUv(i, sprite.getU(quad.getU(i)), sprite.getV(quad.getV(i)));
        }
    }

    private static void applyModifier(QuadEmitter quad, VertexModifier modifier) {
        for (int i = 0; i < 4; i++) {
            modifier.apply(quad, i);
        }
    }

    @FunctionalInterface
    private interface VertexModifier {
        void apply(QuadEmitter quad, int vertexIndex);
    }
}
