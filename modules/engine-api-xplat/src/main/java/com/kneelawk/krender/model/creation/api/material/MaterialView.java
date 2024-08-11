package com.kneelawk.krender.model.creation.api.material;

import org.jetbrains.annotations.ApiStatus;

import com.kneelawk.krender.model.creation.api.TriState;

/**
 * Provides access to the values of a material.
 * <p>
 * Values returned may not match those set, but should be more accurate to the material actually rendered.
 */
@ApiStatus.NonExtendable
public interface MaterialView {
    /**
     * {@return this material's blend mode}
     */
    BlendMode getBlendMode();

    /**
     * {@return whether this material disables color indices}
     */
    boolean isColorIndexDisabled();

    /**
     * {@return whether this material is emissive}
     * <p>
     * Emissive materials always render at full-brightness, ignoring provided lightmap values.
     */
    boolean isEmissive();

    /**
     * {@return whether diffuse shading is disabled for this material}
     */
    boolean isDiffuseDisabled();

    /**
     * {@return whether ambient occlusion is enabled}
     */
    TriState getAmbientOcclusionMode();
}
