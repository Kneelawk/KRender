package com.kneelawk.krender.engine.base.texture;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;

import com.kneelawk.krender.engine.api.texture.MaterialTexture;
import com.kneelawk.krender.engine.api.texture.SpriteFinder;
import com.kneelawk.krender.reloadlistener.api.ReloadSensitiveLazy;

/**
 * Base {@link MaterialTexture} implementation.
 */
public abstract class BaseMaterialTexture implements MaterialTexture {
    /**
     * The texture's integer id.
     */
    protected final int intId;
    /**
     * The texture's resource location.
     */
    protected final Identifier textureId;
    /**
     * Lazy access to the backing vanilla texture.
     */
    protected final ReloadSensitiveLazy<AbstractTexture> texture;

    /**
     * Creates a new {@link BaseMaterialTexture}.
     *
     * @param textureId the resource location of the backing texture.
     * @param intId     the integer id of this texture.
     */
    protected BaseMaterialTexture(Identifier textureId, int intId) {
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
    public Identifier id() {
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
}
