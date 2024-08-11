package com.kneelawk.krender.engine.fabric.impl.material;

import java.util.Objects;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;

import com.kneelawk.krender.engine.api.TriState;
import com.kneelawk.krender.engine.api.material.BlendMode;
import com.kneelawk.krender.engine.api.material.MaterialView;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.fabric.impl.ConversionUtils;

public class FabricMaterialFinder implements com.kneelawk.krender.engine.api.material.MaterialFinder {
    private static final ThreadLocal<FabricMaterialFinder> FINDER_POOL =
        ThreadLocal.withInitial(FabricMaterialFinder::new);

    public static FabricMaterialFinder getOrCreate() {
        return FINDER_POOL.get().clear();
    }

    public final MaterialFinder finder = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer(),
        "No Fabric Render API implementation available! Did you for get to install Indium?").materialFinder();

    @Override
    public RenderMaterial find() {
        return FabricRenderMaterial.getOrCreate(finder.find());
    }

    @Override
    public FabricMaterialFinder clear() {
        finder.clear();
        return this;
    }

    @Override
    public FabricMaterialFinder setBlendMode(BlendMode blendMode) {
        finder.blendMode(ConversionUtils.toFabric(blendMode));
        return this;
    }

    @Override
    public FabricMaterialFinder setColorIndexDisabled(boolean disabled) {
        finder.disableColorIndex(disabled);
        return this;
    }

    @Override
    public FabricMaterialFinder setEmissive(boolean emissive) {
        finder.emissive(emissive);
        return this;
    }

    @Override
    public FabricMaterialFinder setDiffuseDisabled(boolean disabled) {
        finder.disableDiffuse(disabled);
        return this;
    }

    @Override
    public FabricMaterialFinder setAmbientOcclusionMode(TriState mode) {
        finder.ambientOcclusion(ConversionUtils.toFabric(mode));
        return this;
    }

    @Override
    public FabricMaterialFinder copyFrom(MaterialView material) {
        if (material instanceof FabricMaterialFinder fabric) {
            finder.copyFrom(fabric.finder);
        } else if (material instanceof FabricRenderMaterial fabric) {
            finder.copyFrom(fabric.material);
        } else {
            setBlendMode(material.getBlendMode()).setColorIndexDisabled(material.isColorIndexDisabled())
                .setEmissive(material.isEmissive()).setDiffuseDisabled(material.isDiffuseDisabled())
                .setAmbientOcclusionMode(material.getAmbientOcclusionMode());
        }
        return this;
    }

    @Override
    public BlendMode getBlendMode() {
        return ConversionUtils.toKRender(finder.blendMode());
    }

    @Override
    public boolean isColorIndexDisabled() {
        return finder.disableColorIndex();
    }

    @Override
    public boolean isEmissive() {
        return finder.emissive();
    }

    @Override
    public boolean isDiffuseDisabled() {
        return finder.disableDiffuse();
    }

    @Override
    public TriState getAmbientOcclusionMode() {
        return ConversionUtils.toKRender(finder.ambientOcclusion());
    }
}
