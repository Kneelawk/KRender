package com.kneelawk.krender.engine.backend.test.impl;

import org.jspecify.annotations.Nullable;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.base.material.BaseMaterialView;
import com.kneelawk.krender.engine.base.material.BaseRenderMaterial;

public class TestRenderMaterial extends BaseRenderMaterial {
    /**
     * Creates a new {@link TestRenderMaterial} with the given bits.
     *
     * @param finder the material finder used to create this material.
     * @param intId  the integer id of the render material.
     */
    public TestRenderMaterial(BaseMaterialView finder, int intId) {
        super(finder, intId);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return TestRenderer.INSTANCE;
    }
}
