package com.kneelawk.krender.engine.backend.neoforge.impl;

import org.jetbrains.annotations.NotNull;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.convert.TypeConverter;
import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.mesh.MeshBuilder;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.api.model.BakedModelUnwrapper;
import com.kneelawk.krender.engine.api.texture.MaterialTextureManager;
import com.kneelawk.krender.engine.backend.neoforge.impl.material.NFRenderMaterial;
import com.kneelawk.krender.engine.backend.neoforge.impl.mesh.NFMeshBuilder;
import com.kneelawk.krender.engine.backend.neoforge.impl.model.NFBakedModelFactory;
import com.kneelawk.krender.engine.backend.neoforge.impl.model.NFUnwrapper;
import com.kneelawk.krender.engine.backend.neoforge.impl.texture.NFMaterialTexture;
import com.kneelawk.krender.engine.base.convert.BaseTypeConverter;
import com.kneelawk.krender.engine.base.material.BaseMaterialManager;
import com.kneelawk.krender.engine.base.texture.BaseMaterialTextureManager;

public class NFRenderer implements KRenderer {
    public static final NFRenderer INSTANCE = new NFRenderer();

    public final NFUnwrapper unwrapper = new NFUnwrapper();
    public final BaseMaterialTextureManager textureManager =
        new BaseMaterialTextureManager(this, NFMaterialTexture::new);
    public final BaseMaterialManager materialManager = new BaseMaterialManager(this, NFRenderMaterial::new);
    public final BaseTypeConverter typeConverter = new BaseTypeConverter(this);

    @Override
    public @NotNull BakedModelFactory bakedModelFactory() {
        return new NFBakedModelFactory();
    }

    @Override
    public @NotNull BakedModelUnwrapper bakedModelUnwrapper() {
        return unwrapper;
    }

    @Override
    public @NotNull MeshBuilder meshBuilder() {
        return new NFMeshBuilder(this);
    }

    @Override
    public @NotNull MaterialManager materialManager() {
        return materialManager;
    }

    @Override
    public @NotNull MaterialTextureManager textureManager() {
        return textureManager;
    }

    @Override
    public @NotNull TypeConverter converter() {
        return typeConverter;
    }
}
