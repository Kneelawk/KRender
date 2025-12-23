package com.kneelawk.krender.model.guard.impl;

import net.minecraft.resources.Identifier;

public class KRMGConstants {
    public static final String MOD_ID = "krender_model_guard";
    public static final String PARENT_MOD_ID = "krender";

    public static final Identifier LOADER_NAME = prl("gltf");

    public static Identifier rl(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static Identifier prl(String path) {
        return Identifier.fromNamespaceAndPath(PARENT_MOD_ID, path);
    }
}
