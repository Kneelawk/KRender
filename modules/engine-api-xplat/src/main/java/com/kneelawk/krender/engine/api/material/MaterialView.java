package com.kneelawk.krender.engine.api.material;

import com.kneelawk.krender.engine.api.RendererDependent;
import com.kneelawk.krender.engine.api.TriState;

/**
 * Provides access to the values of a material.
 * <p>
 * Values returned may not match those set, but should be more accurate to the material actually rendered.
 */
public interface MaterialView extends RendererDependent {
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
