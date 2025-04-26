package com.kneelawk.krender.engine.backend.shim;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.convert.TypeConverter;
import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.mesh.MeshBuilder;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.api.model.BakedModelUnwrapper;
import com.kneelawk.krender.engine.api.texture.MaterialTextureManager;
import com.kneelawk.krender.engine.backend.shim.material.ShimRenderMaterial;
import com.kneelawk.krender.engine.backend.shim.texture.ShimMaterialTexture;
import com.kneelawk.krender.engine.base.material.BaseMaterialManager;
import com.kneelawk.krender.engine.base.mesh.BaseMeshBuilder;
import com.kneelawk.krender.engine.base.texture.BaseMaterialTextureManager;

public class ShimRenderer implements KRenderer {
    public static final ShimRenderer INSTANCE = new ShimRenderer();

    private final BaseMaterialTextureManager textureManager =
        new BaseMaterialTextureManager(this, ShimMaterialTexture::new);
    private final BaseMaterialManager materialManager = new BaseMaterialManager(this, ShimRenderMaterial::new);

    @Override
    public BakedModelFactory bakedModelFactory() {
        return null;
    }

    @Override
    public BakedModelUnwrapper bakedModelUnwrapper() {
        return null;
    }

    @Override
    public MeshBuilder meshBuilder() {
        return new BaseMeshBuilder(this);
    }

    @Override
    public MaterialManager materialManager() {
        return materialManager;
    }

    @Override
    public MaterialTextureManager textureManager() {
        return textureManager;
    }

    @Override
    public TypeConverter converter() {
        return null;
    }
}
