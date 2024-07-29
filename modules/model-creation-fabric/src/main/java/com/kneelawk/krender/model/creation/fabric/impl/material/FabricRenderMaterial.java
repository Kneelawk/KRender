package com.kneelawk.krender.model.creation.fabric.impl.material;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

import com.kneelawk.krender.model.creation.api.TriState;
import com.kneelawk.krender.model.creation.api.material.BlendMode;
import com.kneelawk.krender.model.creation.fabric.impl.ConversionUtils;

public record FabricRenderMaterial(RenderMaterial material)
    implements com.kneelawk.krender.model.creation.api.material.RenderMaterial {

    @Override
    public BlendMode getBlendMode() {
        return ConversionUtils.toKRender(material.blendMode());
    }

    @Override
    public boolean isColorIndexDisabled() {
        return material.disableColorIndex();
    }

    @Override
    public boolean isEmissive() {
        return material.emissive();
    }

    @Override
    public boolean isDiffuseDisabled() {
        return material.disableDiffuse();
    }

    @Override
    public TriState getAmbientOcclusionMode() {
        return ConversionUtils.toKRender(material.ambientOcclusion());
    }
}
