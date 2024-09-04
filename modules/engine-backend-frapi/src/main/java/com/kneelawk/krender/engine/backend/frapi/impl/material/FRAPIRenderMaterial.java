package com.kneelawk.krender.engine.backend.frapi.impl.material;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.TriState;
import com.kneelawk.krender.engine.api.material.BlendMode;
import com.kneelawk.krender.engine.backend.frapi.api.ConversionUtils;
import com.kneelawk.krender.engine.backend.frapi.impl.FRAPIRenderer;

public final class FRAPIRenderMaterial
    implements com.kneelawk.krender.engine.api.material.RenderMaterial {
    private static final ConcurrentHashMap<RenderMaterial, FRAPIRenderMaterial> CACHE = new ConcurrentHashMap<>();
    public static final FRAPIRenderMaterial DEFAULT = getOrCreate(
        Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer(), "no renderer").materialFinder().clear().find());

    public static FRAPIRenderMaterial getOrCreate(RenderMaterial material) {
        return CACHE.computeIfAbsent(material, FRAPIRenderMaterial::new);
    }

    public final RenderMaterial material;

    private FRAPIRenderMaterial(RenderMaterial material) {this.material = material;}

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

    @Override
    public @Nullable KRenderer getRenderer() {
        return FRAPIRenderer.INSTNACE;
    }
}
