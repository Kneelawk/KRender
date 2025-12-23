package com.kneelawk.krender.engine.backend.frapi.impl;

import org.jetbrains.annotations.NotNull;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.convert.TypeConverter;
import com.kneelawk.krender.engine.api.mesh.MeshBuilder;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.api.model.BakedModelUnwrapper;
import com.kneelawk.krender.engine.api.texture.MaterialTextureManager;
import com.kneelawk.krender.engine.backend.frapi.impl.material.FRAPIMaterialManager;
import com.kneelawk.krender.engine.backend.frapi.impl.model.FRAPIBakedModelFactory;
import com.kneelawk.krender.engine.backend.frapi.impl.model.FRAPIUnwrapper;
import com.kneelawk.krender.engine.backend.frapi.impl.texture.FRAPIMaterialTexture;
import com.kneelawk.krender.engine.base.convert.BaseTypeConverter;
import com.kneelawk.krender.engine.base.mesh.BaseMeshBuilder;
import com.kneelawk.krender.engine.base.texture.BaseMaterialTextureManager;

public class FRAPIRenderer implements KRenderer {
    public static final FRAPIRenderer INSTNACE = new FRAPIRenderer();

    private final FRAPIUnwrapper unwrapper = new FRAPIUnwrapper();
    private final BaseMaterialTextureManager textureManager =
        new BaseMaterialTextureManager(this, FRAPIMaterialTexture::new);
    private final FRAPIMaterialManager materialManager = new FRAPIMaterialManager();
    private final BaseTypeConverter typeConverter = new BaseTypeConverter(this);

    @Override
    public @NotNull BakedModelFactory bakedModelFactory() {
        return new FRAPIBakedModelFactory();
    }

    @Override
    public @NotNull BakedModelUnwrapper bakedModelUnwrapper() {
        return unwrapper;
    }

    @Override
    public @NotNull MeshBuilder meshBuilder() {
        return new BaseMeshBuilder(this);
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
