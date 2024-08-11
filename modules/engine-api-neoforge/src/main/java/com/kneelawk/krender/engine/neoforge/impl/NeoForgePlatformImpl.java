package com.kneelawk.krender.engine.neoforge.impl;

import net.neoforged.fml.loading.FMLLoader;

import com.kneelawk.krender.engine.impl.Platform;

public class NeoForgePlatformImpl implements Platform {
    @Override
    public boolean isModLoaded(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }
}
