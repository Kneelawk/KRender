package com.kneelawk.krender.engine.api.texture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.engine.api.RendererDependent;

/**
 * A texture managed by a KRender backend for use in a {@link com.kneelawk.krender.engine.api.material.MaterialView}.
 * <p>
 * {@link MaterialTexture}s are not tied to any specific {@link AbstractTexture} but are instead tied to a specific
 * {@link ResourceLocation}. The associated {@link AbstractTexture} is updated during every resource-reload.
 */
public interface MaterialTexture extends RendererDependent {
    /**
     * {@return an integer id of this texture}
     * <p>
     * This is only guaranteed to be the same for a single runtime. Textures' integer ids are likely to change across
     * restarts.
     */
    int intId();

    /**
     * {@return this material texture's id}
     * <p>
     * This material texture's id is the same as the id of the minecraft texture this material texture represents if any.
     */
    ResourceLocation id();

    /**
     * {@return the minecraft texture backing this material texture}
     */
    AbstractTexture texture();

    /**
     * {@return whether this texture is an atlas}
     */
    boolean isAtlas();

    /**
     * {@return this texture as a texture atlas if this texture is backed by a texture atlas}
     */
    @Nullable TextureAtlas textureAtlas();

    /**
     * {@return the sprite finder for the atlas associated with this texture if any}
     */
    @Nullable SpriteFinder spriteFinder();
}
