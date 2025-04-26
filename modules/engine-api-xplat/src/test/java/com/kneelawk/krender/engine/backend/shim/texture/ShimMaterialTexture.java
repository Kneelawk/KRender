package com.kneelawk.krender.engine.backend.shim.texture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.backend.shim.ShimRenderer;
import com.kneelawk.krender.engine.base.texture.BaseMaterialTexture;

public class ShimMaterialTexture extends BaseMaterialTexture {
    /**
     * Creates a new {@link ShimMaterialTexture}.
     *
     * @param textureId the resource location of the backing texture.
     * @param intId     the integer id of this texture.
     */
    public ShimMaterialTexture(ResourceLocation textureId, int intId) {
        super(textureId, intId);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return ShimRenderer.INSTANCE;
    }
}
