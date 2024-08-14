package com.kneelawk.krender.engine.backend.frapi.impl.material;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;

import com.kneelawk.krender.engine.api.TriState;
import com.kneelawk.krender.engine.api.material.BlendMode;
import com.kneelawk.krender.engine.api.material.MaterialView;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.backend.frapi.api.ConversionUtils;

public class FRAPIMaterialFinder implements com.kneelawk.krender.engine.api.material.MaterialFinder {
    private static final ThreadLocal<FRAPIMaterialFinder> FINDER_POOL =
        ThreadLocal.withInitial(FRAPIMaterialFinder::new);

    public static FRAPIMaterialFinder getOrCreate() {
        return FINDER_POOL.get().clear();
    }

    public final MaterialFinder finder = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer(),
        "No Fabric Render API implementation available! Did you for get to install Indium?").materialFinder();

    @Override
    public @NotNull RenderMaterial find() {
        return FRAPIRenderMaterial.getOrCreate(finder.find());
    }

    @Override
    public FRAPIMaterialFinder clear() {
        finder.clear();
        return this;
    }

    @Override
    public FRAPIMaterialFinder setBlendMode(BlendMode blendMode) {
        finder.blendMode(ConversionUtils.toFabric(blendMode));
        return this;
    }

    @Override
    public FRAPIMaterialFinder setColorIndexDisabled(boolean disabled) {
        finder.disableColorIndex(disabled);
        return this;
    }

    @Override
    public FRAPIMaterialFinder setEmissive(boolean emissive) {
        finder.emissive(emissive);
        return this;
    }

    @Override
    public FRAPIMaterialFinder setDiffuseDisabled(boolean disabled) {
        finder.disableDiffuse(disabled);
        return this;
    }

    @Override
    public FRAPIMaterialFinder setAmbientOcclusionMode(TriState mode) {
        finder.ambientOcclusion(ConversionUtils.toFabric(mode));
        return this;
    }

    @Override
    public FRAPIMaterialFinder copyFrom(MaterialView material) {
        if (material instanceof FRAPIMaterialFinder fabric) {
            finder.copyFrom(fabric.finder);
        } else if (material instanceof FRAPIRenderMaterial fabric) {
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
