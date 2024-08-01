package com.kneelawk.krender.model.creation.fabric.impl.material;

import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

import com.kneelawk.krender.model.creation.api.TriState;
import com.kneelawk.krender.model.creation.api.material.BlendMode;
import com.kneelawk.krender.model.creation.fabric.impl.ConversionUtils;

public final class FabricRenderMaterial
    implements com.kneelawk.krender.model.creation.api.material.RenderMaterial {
    private static final ConcurrentHashMap<RenderMaterial, FabricRenderMaterial> CACHE = new ConcurrentHashMap<>();

    public static FabricRenderMaterial getOrCreate(RenderMaterial material) {
        return CACHE.computeIfAbsent(material, FabricRenderMaterial::new);
    }

    public final RenderMaterial material;

    private FabricRenderMaterial(RenderMaterial material) {this.material = material;}

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
