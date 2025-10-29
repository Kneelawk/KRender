package com.kneelawk.krender.engine.fabric.impl;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;

import com.kneelawk.krender.engine.impl.Platform;

public class FabricPlatformImpl implements Platform {
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
