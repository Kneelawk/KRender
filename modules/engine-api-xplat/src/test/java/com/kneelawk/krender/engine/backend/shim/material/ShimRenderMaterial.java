package com.kneelawk.krender.engine.backend.shim.material;

import org.jetbrains.annotations.Nullable;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.backend.shim.ShimRenderer;
import com.kneelawk.krender.engine.base.material.BaseMaterialView;
import com.kneelawk.krender.engine.base.material.BaseRenderMaterial;

public class ShimRenderMaterial extends BaseRenderMaterial {

    /**
     * Creates a new {@link BaseRenderMaterial} with the given bits.
     *
     * @param finder the material finder used to create this material.
     * @param intId  the integer id of the render material.
     */
    public ShimRenderMaterial(BaseMaterialView finder, int intId) {
        super(finder, intId);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return ShimRenderer.INSTANCE;
    }
}
