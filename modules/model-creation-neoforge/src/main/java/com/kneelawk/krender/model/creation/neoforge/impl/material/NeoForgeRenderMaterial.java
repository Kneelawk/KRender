package com.kneelawk.krender.model.creation.neoforge.impl.material;

import com.kneelawk.krender.model.creation.api.TriState;
import com.kneelawk.krender.model.creation.api.material.BlendMode;
import com.kneelawk.krender.model.creation.api.material.RenderMaterial;

public class NeoForgeRenderMaterial implements RenderMaterial {
    private final BlendMode blendMode;
    private final boolean colorIndexDisabled;
    private final boolean emissive;
    private final boolean diffuseDisabled;
    private final TriState ambientOcclusion;

    public NeoForgeRenderMaterial(BlendMode blendMode, boolean colorIndexDisabled, boolean emissive,
                                  boolean diffuseDisabled, TriState ambientOcclusion) {
        this.blendMode = blendMode;
        this.colorIndexDisabled = colorIndexDisabled;
        this.emissive = emissive;
        this.diffuseDisabled = diffuseDisabled;
        this.ambientOcclusion = ambientOcclusion;
    }

    @Override
    public BlendMode getBlendMode() {
        return blendMode;
    }

    @Override
    public boolean isColorIndexDisabled() {
        return colorIndexDisabled;
    }

    @Override
    public boolean isEmissive() {
        return emissive;
    }

    @Override
    public boolean isDiffuseDisabled() {
        return diffuseDisabled;
    }

    @Override
    public TriState getAmbientOcclusionMode() {
        return ambientOcclusion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NeoForgeRenderMaterial that = (NeoForgeRenderMaterial) o;
        return colorIndexDisabled == that.colorIndexDisabled && emissive == that.emissive &&
            diffuseDisabled == that.diffuseDisabled && blendMode == that.blendMode &&
            ambientOcclusion == that.ambientOcclusion;
    }

    @Override
    public int hashCode() {
        int result = blendMode.hashCode();
        result = 31 * result + Boolean.hashCode(colorIndexDisabled);
        result = 31 * result + Boolean.hashCode(emissive);
        result = 31 * result + Boolean.hashCode(diffuseDisabled);
        result = 31 * result + ambientOcclusion.hashCode();
        return result;
    }
}
