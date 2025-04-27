package com.kneelawk.krender.engine.backend.test.impl;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.convert.TypeConverter;
import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.mesh.MeshBuilder;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.api.model.BakedModelUnwrapper;
import com.kneelawk.krender.engine.api.texture.MaterialTextureManager;
import com.kneelawk.krender.engine.base.convert.BaseTypeConverter;
import com.kneelawk.krender.engine.base.material.BaseMaterialManager;
import com.kneelawk.krender.engine.base.mesh.BaseMeshBuilder;
import com.kneelawk.krender.engine.base.texture.BaseMaterialTextureManager;

public class TestRenderer implements KRenderer {
    public static final TestRenderer INSTANCE = new TestRenderer();

    private final BaseMaterialTextureManager textureManager =
        new BaseMaterialTextureManager(this, TestMaterialTexture::new);
    private final BaseMaterialManager materialManager = new BaseMaterialManager(this, TestRenderMaterial::new);
    private final BaseTypeConverter typeConverter = new BaseTypeConverter(this);

    @Override
    public BakedModelFactory bakedModelFactory() {
        return new TestBakedModelFactory();
    }

    @Override
    public BakedModelUnwrapper bakedModelUnwrapper() {
        return model -> null;
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
        return typeConverter;
    }
}
