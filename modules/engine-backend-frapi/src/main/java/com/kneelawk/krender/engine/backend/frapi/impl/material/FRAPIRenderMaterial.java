package com.kneelawk.krender.engine.backend.frapi.impl.material;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.backend.frapi.api.ConversionUtils;
import com.kneelawk.krender.engine.backend.frapi.impl.FRAPIRenderer;
import com.kneelawk.krender.engine.base.material.BaseMaterialView;

public class FRAPIRenderMaterial extends BaseMaterialView implements RenderMaterial {
    public final net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial material;

    public FRAPIRenderMaterial(int bits) {
        super(bits);
        material =
            Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer(), "Missing FRAPI renderer").materialFinder()
                .blendMode(ConversionUtils.toFabric(getBlendMode())).disableColorIndex(isColorIndexDisabled())
                .emissive(isEmissive()).disableDiffuse(isDiffuseDisabled())
                .ambientOcclusion(ConversionUtils.toFabric(getAmbientOcclusionMode())).find();
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return FRAPIRenderer.INSTNACE;
    }
}
