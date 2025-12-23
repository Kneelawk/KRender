package com.kneelawk.krender.engine.backend.neoforge.impl.material;

import org.jspecify.annotations.Nullable;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.backend.neoforge.impl.NFRenderer;
import com.kneelawk.krender.engine.base.material.BaseMaterialView;
import com.kneelawk.krender.engine.base.material.BaseRenderMaterial;

public class NFRenderMaterial extends BaseRenderMaterial {

    /**
     * Creates a new {@link NFRenderMaterial} with the given bits.
     *
     * @param finder the material finder used to create this material.
     * @param intId the integer id of the render material.
     */
    public NFRenderMaterial(BaseMaterialView finder, int intId) {
        super(finder, intId);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return NFRenderer.INSTANCE;
    }
}
