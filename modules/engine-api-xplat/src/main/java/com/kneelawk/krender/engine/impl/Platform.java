package com.kneelawk.krender.engine.impl;

import java.util.ServiceLoader;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.BakedModelCore;
import com.kneelawk.krender.engine.api.material.MaterialFinder;

public interface Platform {
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst()
        .orElseThrow(() -> new RuntimeException("Unable to find KRender Model Creation platform"));

    BakedModel wrap(BakedModelCore core);

    MaterialFinder getOrCreateMaterialFinder();
}
