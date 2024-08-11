package com.kneelawk.krender.engine.impl;

import java.util.ServiceLoader;

public interface Platform {
    Platform INSTANCE = ServiceLoader.load(Platform.class).findFirst()
        .orElseThrow(() -> new RuntimeException("Unable to find KRender Model Creation platform"));

    boolean isModLoaded(String modId);
}
