package com.kneelawk.krender.ctcomplicated.fabric;

import net.fabricmc.api.ModInitializer;

import com.kneelawk.krender.ctcomplicated.CTComplicated;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CTComplicated.init();
        CTComplicated.initSync();
        CTComplicated.REGISTRARS.registerAll();
    }
}
