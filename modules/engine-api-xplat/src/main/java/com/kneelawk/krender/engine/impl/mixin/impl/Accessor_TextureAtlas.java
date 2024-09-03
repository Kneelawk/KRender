package com.kneelawk.krender.engine.impl.mixin.impl;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

@Mixin(TextureAtlas.class)
public interface Accessor_TextureAtlas {
    @Accessor("texturesByName")
    Map<ResourceLocation, TextureAtlasSprite> krender_engine_api$texturesByName();

    @Accessor("width")
    int krender_engine_api$width();

    @Accessor("height")
    int krender_engine_api$height();

    @Accessor("mipLevel")
    int krender_engine_api$mipLevel();
}
