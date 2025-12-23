package com.kneelawk.krender.engine.backend.frapi.impl.texture;

import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.backend.frapi.impl.FRAPIRenderer;
import com.kneelawk.krender.engine.base.texture.BaseMaterialTexture;

public class FRAPIMaterialTexture extends BaseMaterialTexture {
    /**
     * Creates a new {@link FRAPIMaterialTexture}.
     *
     * @param textureId the resource location of the backing texture.
     * @param intId     the integer id of this texture.
     */
    public FRAPIMaterialTexture(Identifier textureId, int intId) {
        super(textureId, intId);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return FRAPIRenderer.INSTNACE;
    }
}
