package com.kneelawk.krender.engine.impl.texture;

import java.util.Map;

import com.google.common.math.IntMath;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.engine.api.texture.SpriteFinder;
import com.kneelawk.krender.engine.impl.mixin.api.Duck_TextureAtlas;
import com.kneelawk.krender.engine.impl.mixin.impl.Accessor_TextureAtlas;

public class SpriteFinderHolder {
    public static SpriteFinder createSpriteFinder(TextureAtlas atlas) {
        Accessor_TextureAtlas accessor = (Accessor_TextureAtlas) atlas;

        Map<ResourceLocation, TextureAtlasSprite> texturesByName = accessor.krender_engine_api$texturesByName();
        TextureAtlasSprite[] sprites = new TextureAtlasSprite[texturesByName.size()];
        boolean powerOf2 = true;
        int minSize = 65536;

        int i = 0;
        for (TextureAtlasSprite sprite : texturesByName.values()) {
            sprites[i++] = sprite;

            int width = sprite.contents().width();
            int height = sprite.contents().height();

            if (!IntMath.isPowerOfTwo(width) || !IntMath.isPowerOfTwo(height)) {
                powerOf2 = false;
            }

            if (width < minSize) {
                minSize = width;
            }
            if (height < minSize) {
                minSize = height;
            }
        }

        if (powerOf2) {
            return new DirectSpriteFinder(atlas, sprites, minSize);
        } else {
            return new QuadTreeSpriteFinder(atlas, sprites);
        }
    }

    public static SpriteFinder getSpriteFinder(TextureAtlas atlas) {
        Duck_TextureAtlas duck = (Duck_TextureAtlas) atlas;
        SpriteFinder finder = duck.krender_engine_api$getSpriteFinder();
        if (finder == null) throw new IllegalStateException("The texture atlas has not been uploaded yet");
        return finder;
    }
}
