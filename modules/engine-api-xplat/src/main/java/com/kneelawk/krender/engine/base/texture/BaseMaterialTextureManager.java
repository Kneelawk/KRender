package com.kneelawk.krender.engine.base.texture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.texture.MaterialTexture;
import com.kneelawk.krender.engine.api.texture.MaterialTextureManager;

public class BaseMaterialTextureManager implements MaterialTextureManager {
    
    
    @Override
    public MaterialTexture textureByIntId(int id) {
        return null;
    }

    @Override
    public int maxIntId() {
        return 0;
    }

    @Override
    public MaterialTexture none() {
        return null;
    }

    @Override
    public MaterialTexture missing() {
        return null;
    }

    @Override
    public MaterialTexture blockAtlas() {
        return null;
    }

    @Override
    public @Nullable MaterialTexture textureById(ResourceLocation id) {
        return null;
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return null;
    }
}
