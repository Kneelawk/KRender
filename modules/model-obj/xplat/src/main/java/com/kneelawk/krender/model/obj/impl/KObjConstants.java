package com.kneelawk.krender.model.obj.impl;

import net.minecraft.resources.Identifier;

public class KObjConstants {
    public static final String MOD_ID = "krender_model_obj";
    public static final String PARENT_ID = "krender";

    public static final Identifier LOADER_ID = prl("obj");

    public static Identifier prl(String path) {
        return Identifier.fromNamespaceAndPath(PARENT_ID, path);
    }
}
