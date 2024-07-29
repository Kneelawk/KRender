package com.kneelawk.krender.model.creation.api.material;

import org.jetbrains.annotations.ApiStatus;

import com.kneelawk.krender.model.creation.api.TriState;
import com.kneelawk.krender.model.creation.impl.Platform;

/**
 * Used for finding/building materials.
 */
@ApiStatus.NonExtendable
public interface MaterialFinder extends MaterialView {
    /**
     * Gets or creates the render material specified by this material finder.
     *
     * @return the requested material.
     */
    RenderMaterial find();

    /**
     * Resets this material finder back to its default values.
     *
     * @return this material finder.
     */
    MaterialFinder clear();

    /**
     * Sets this material's blend mode.
     *
     * @param blendMode the new blend mode.
     * @return this material finder.
     */
    MaterialFinder setBlendMode(BlendMode blendMode);

    /**
     * Tint indices are enabled by default. Use this method to disable them.
     *
     * @param disabled whether to disable tint indices.
     * @return this material finder.
     */
    MaterialFinder setColorIndexDisabled(boolean disabled);

    /**
     * Sets whether this material is emissive. This causes it to ignore lighting values.
     *
     * @param emissive the emissive value.
     * @return this material finder.
     */
    MaterialFinder setEmissive(boolean emissive);

    /**
     * Sets whether diffuse shading is disabled.
     *
     * @param disabled whether to disable diffuse shading.
     * @return this material finder.
     */
    MaterialFinder setDiffuseDisabled(boolean disabled);

    /**
     * Sets whether ambient occlusion is force enabled, disabled, or left up to the model.
     *
     * @param mode whether ambient occlusion is force enabled, disabled, or left up to the model.
     * @return this material finder.
     */
    MaterialFinder setAmbientOcclusionMode(TriState mode);

    /**
     * Copies all properties from the given material.
     *
     * @param material the material to copy from.
     * @return this material finder.
     */
    MaterialFinder copyFrom(MaterialView material);
    
    static MaterialFinder get() {
        return Platform.INSTANCE.getOrCreateMaterialFinder();
    }
}
