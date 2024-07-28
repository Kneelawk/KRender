package com.kneelawk.krender.model.creation.impl;

import java.util.ServiceLoader;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.model.creation.api.BakedModelCore;

public interface Platform {
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst()
        .orElseThrow(() -> new RuntimeException("Unable to find KRender Model Creation platform"));

    BakedModel wrap(BakedModelCore core);
}
