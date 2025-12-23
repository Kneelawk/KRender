package com.kneelawk.krender.engine.backend.test.impl;

import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.base.texture.BaseMaterialTexture;

public class TestMaterialTexture extends BaseMaterialTexture {
    /**
     * Creates a new {@link TestMaterialTexture}.
     *
     * @param textureId the resource location of the backing texture.
     * @param intId     the integer id of this texture.
     */
    public TestMaterialTexture(Identifier textureId, int intId) {
        super(textureId, intId);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return TestRenderer.INSTANCE;
    }
}
