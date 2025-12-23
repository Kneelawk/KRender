package com.kneelawk.krender.engine.backend.neoforge.impl.texture;

import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.backend.neoforge.impl.NFRenderer;
import com.kneelawk.krender.engine.base.texture.BaseMaterialTexture;

public class NFMaterialTexture extends BaseMaterialTexture {
    /**
     * Creates a new {@link NFMaterialTexture}.
     *
     * @param textureId the resource location of the backing texture.
     * @param intId     the integer id of this texture.
     */
    public NFMaterialTexture(Identifier textureId, int intId) {
        super(textureId, intId);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return NFRenderer.INSTANCE;
    }
}
