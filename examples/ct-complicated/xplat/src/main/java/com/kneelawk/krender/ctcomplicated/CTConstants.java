package com.kneelawk.krender.ctcomplicated;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public class CTConstants {
    public static final String MOD_ID = "ct_complicated";

    public static Identifier rl(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static MutableComponent tt(String prefix, String suffix, Object... args) {
        return Component.translatable(prefix + "." + MOD_ID + "." + suffix, args);
    }
}
