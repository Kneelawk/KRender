package com.kneelawk.krender.engine.impl;

import net.minecraft.resources.Identifier;

public class KREConstants {
    public static final String MOD_ID = "krender_engine_api";
    public static final String CONFIG_DIR = "krender";
    public static final String PARENT_MOD_ID = "krender";

    public static Identifier prl(String path) {
        return Identifier.fromNamespaceAndPath(PARENT_MOD_ID, path);
    }
}
