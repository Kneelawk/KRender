package com.kneelawk.krender.model.creation.api;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;

/**
 * Implementors of this interface function as cross-platform {@link BakedModel}s.
 * <p>
 * This interface abstracts away the loader-platform-specific extra methods that can be implemented on {@link BakedModel}s.
 */
public interface BakedModelCore {
    /**
     * {@return whether this model has ambient occlusion}
     */
    boolean useAmbientOcclusion();

    /**
     * {@return whether this model should appear rotated in inventory GUIs}
     */
    boolean isGui3d();

    /**
     * {@return whether this model is lit from the side ({@code true}) or head-on ({@code false})}
     */
    boolean usesBlockLight();

    /**
     * {@return whether this model is hardcoded ({@code true}) or resource-based ({@code false})}
     */
    boolean isCustomRenderer();

    /**
     * {@return the particle sprite for this model}
     */
    TextureAtlasSprite getParticleIcon();

    /**
     * {@return the rotation of this model as an item}
     */
    ItemTransforms getTransforms();

    /**
     * {@return the baked model item overrides}
     */
    ItemOverrides getOverrides();
}
