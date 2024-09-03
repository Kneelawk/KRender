package com.kneelawk.krender.engine.api.texture;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.kneelawk.krender.engine.api.buffer.QuadView;
import com.kneelawk.krender.engine.impl.texture.SpriteFinderHolder;

/**
 * Platform independent tool for finding a {@link TextureAtlasSprite} when given texture coordinates in a {@link TextureAtlas}.
 */
public interface SpriteFinder {
    /**
     * Gets the sprite finder for the given atlas.
     *
     * @param atlas the atlas to get the sprite finder of.
     * @return the sprite finder for the given texture atlas.
     */
    static SpriteFinder get(TextureAtlas atlas) {
        return SpriteFinderHolder.getSpriteFinder(atlas);
    }

    /**
     * Convenience method for finding the sprite of a given quad.
     *
     * @param quad the quad to find the sprite of.
     * @return the found sprite for the given quad or the missing-texture sprite if none was found.
     */
    default TextureAtlasSprite find(QuadView quad) {
        float u = quad.getU(0) + quad.getU(1) + quad.getU(2) + quad.getU(3);
        float v = quad.getV(0) + quad.getV(1) + quad.getV(2) + quad.getV(3);
        return find(u * 0.25f, v * 0.25f);
    }

    /**
     * Find a texture atlas sprite given texture coordinates within that sprite.
     *
     * @param u the horizontal position within the sprite.
     * @param v the vertical position within the sprite.
     * @return the found sprite or the missing-texture sprite if none was found.
     */
    TextureAtlasSprite find(float u, float v);
}
