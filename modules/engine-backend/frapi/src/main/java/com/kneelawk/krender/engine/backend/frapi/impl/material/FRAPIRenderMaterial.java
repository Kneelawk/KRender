package com.kneelawk.krender.engine.backend.frapi.impl.material;

import org.jspecify.annotations.Nullable;

import net.fabricmc.fabric.api.renderer.v1.Renderer;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.backend.frapi.api.ConversionUtils;
import com.kneelawk.krender.engine.backend.frapi.impl.FRAPIRenderer;

public class FRAPIRenderMaterial extends BaseRenderMaterial {
    public final net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial material;

    /**
     * Creates a new {@link BaseRenderMaterial} with the given bits.
     *
     * @param finder the material finder used to create this material.
     * @param intId  the integer id of the render material.
     */
    public FRAPIRenderMaterial(BaseMaterialView finder, int intId) {
        super(finder, intId);
        material =
            Renderer.get().materialFinder().blendMode(ConversionUtils.toFabric(getBlendMode())).emissive(isEmissive())
                .disableDiffuse(isDiffuseDisabled())
                .ambientOcclusion(ConversionUtils.toFabric(getAmbientOcclusionMode())).find();
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return FRAPIRenderer.INSTNACE;
    }
}
