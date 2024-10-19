package com.kneelawk.krender.model.obj.fabric.impl;

import net.fabricmc.api.ClientModInitializer;

import com.kneelawk.krender.model.obj.impl.KObj;

public class FabricClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KObj.register();
    }
}
