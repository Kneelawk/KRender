package com.kneelawk.krender.engine.base.texture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.texture.MaterialTexture;
import com.kneelawk.krender.engine.api.texture.SpriteFinder;
import com.kneelawk.krender.reloadlistener.api.ReloadSensitiveLazy;

public class BaseMaterialTexture implements MaterialTexture {
    private final KRenderer renderer;
    private final int intId;
    private final ResourceLocation textureId;
    private final ReloadSensitiveLazy<AbstractTexture> texture;

    protected BaseMaterialTexture(KRenderer renderer, int intId, ResourceLocation textureId) {
        this.renderer = renderer;
        this.intId = intId;
        this.textureId = textureId;
        this.texture =
            new ReloadSensitiveLazy<>(() -> Minecraft.getInstance().getTextureManager().getTexture(textureId));
    }

    @Override
    public int intId() {
        return intId;
    }

    @Override
    public ResourceLocation id() {
        return textureId;
    }

    @Override
    public AbstractTexture texture() {
        return texture.get();
    }

    @Override
    public boolean isAtlas() {
        return texture() instanceof TextureAtlas;
    }

    @Override
    public @Nullable TextureAtlas textureAtlas() {
        if (texture() instanceof TextureAtlas atlas) return atlas;
        return null;
    }

    @Override
    public @Nullable SpriteFinder spriteFinder() {
        TextureAtlas atlas = textureAtlas();
        if (atlas == null) return null;
        return SpriteFinder.get(atlas);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return renderer;
    }
}
