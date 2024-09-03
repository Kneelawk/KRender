package com.kneelawk.krender.engine.impl.texture;

import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.kneelawk.krender.engine.api.texture.SpriteFinder;
import com.kneelawk.krender.engine.impl.mixin.impl.Accessor_TextureAtlas;

/**
 * Fast, array-based sprite finder.
 * <p>
 * This sprite finder relies on all sprites in an atlas having sizes that are powers of 2.
 */
public class DirectSpriteFinder implements SpriteFinder {
    private final TextureAtlas atlas;
    private final TextureAtlasSprite[] sprites;
    private final int gridWidth;
    private final int gridHeight;
    private final int[] grid;

    public DirectSpriteFinder(TextureAtlas atlas, TextureAtlasSprite[] sprites, int minSize) {
        // The sprite finder holder will determine which impl to use.
        // This impl relies on sprites having sizes that are powers of 2.

        this.atlas = atlas;
        this.sprites = sprites;

        Accessor_TextureAtlas accessor = (Accessor_TextureAtlas) atlas;

        int atlasWidth = accessor.krender_engine_api$width();
        int atlasHeight = accessor.krender_engine_api$height();
        gridWidth = atlasWidth / minSize;
        gridHeight = atlasHeight / minSize;

        grid = new int[gridWidth * gridHeight];

        for (int i = 0; i < sprites.length; i++) {
            TextureAtlasSprite sprite = sprites[i];
            int startX = (int) ((sprite.getU0() + 0.00001f) * gridWidth);
            int startY = (int) ((sprite.getV0() + 0.00001f) * gridHeight);
            int width = sprite.contents().width() / minSize;
            int height = sprite.contents().height() / minSize;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int gridIndex = startX + x + (y + startY) * gridWidth;
                    grid[gridIndex] = i + 1;
                }
            }
        }
    }

    @Override
    public TextureAtlasSprite find(float u, float v) {
        int x = (int) (u * gridWidth);
        int y = (int) (v * gridHeight);
        int gridIndex = x + y * gridWidth;
        int index = grid[gridIndex] - 1;

        if (index < 0) return atlas.getSprite(MissingTextureAtlasSprite.getLocation());
        return sprites[index];
    }
}
