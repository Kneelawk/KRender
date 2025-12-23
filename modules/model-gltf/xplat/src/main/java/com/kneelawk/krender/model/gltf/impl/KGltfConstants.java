package com.kneelawk.krender.model.gltf.impl;

import net.minecraft.resources.Identifier;

public class KGltfConstants {
    public static final String MOD_ID = "krender_model_gltf";
    public static final String PARENT_ID = "krender";

    public static final Identifier LOADER_ID = prl("gltf");

    public static Identifier prl(String path) {
        return Identifier.fromNamespaceAndPath(PARENT_ID, path);
    }

    public static Identifier getImageName(Identifier modelId, int imageIndex) {
        return modelId.withSuffix("/images/" + imageIndex);
    }
}
