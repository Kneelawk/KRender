package com.kneelawk.krender.ctcomplicated;

import com.kneelawk.krender.ctcomplicated.block.CTBlocks;
import com.kneelawk.krender.ctcomplicated.misc.CTCreativeTabs;
import com.kneelawk.krender.ctcomplicated.registry.RegistrarSet;

public class CTComplicated {
    public static final RegistrarSet REGISTRARS = new RegistrarSet();

    public static void init() {
        CTBlocks.register();
        CTCreativeTabs.register();
    }

    public static void initSync() {
    }
}
