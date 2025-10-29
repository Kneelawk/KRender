package com.kneelawk.krender.engine.impl.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;

import com.kneelawk.krender.engine.api.texture.SpriteFinder;
import com.kneelawk.krender.engine.impl.mixin.api.Duck_TextureAtlas;
import com.kneelawk.krender.engine.impl.texture.SpriteFinderHolder;

@Mixin(TextureAtlas.class)
public class Mixin_TextureAtlas implements Duck_TextureAtlas {
    @Unique
    private SpriteFinder krender_engine_api$finder;

    @Inject(method = "upload", at = @At("RETURN"))
    private void krender_engine_api$onUpload(SpriteLoader.Preparations preparations, CallbackInfo ci) {
        TextureAtlas atlas = (TextureAtlas) (Object) this;
        krender_engine_api$finder = SpriteFinderHolder.createSpriteFinder(atlas);
    }

    @Unique
    @Override
    public SpriteFinder krender_engine_api$getSpriteFinder() {
        return krender_engine_api$finder;
    }
}
