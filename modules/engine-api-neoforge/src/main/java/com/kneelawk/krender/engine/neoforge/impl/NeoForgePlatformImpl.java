package com.kneelawk.krender.engine.neoforge.impl;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.BakedModelCore;
import com.kneelawk.krender.engine.api.material.MaterialFinder;
import com.kneelawk.krender.engine.impl.Platform;
import com.kneelawk.krender.engine.neoforge.impl.material.NeoForgeMaterialFinder;

public class NeoForgePlatformImpl implements Platform {
    @Override
    public BakedModel wrap(BakedModelCore core) {
        return new NeoForgeBakedModelImpl(core);
    }

    @Override
    public MaterialFinder getOrCreateMaterialFinder() {
        return NeoForgeMaterialFinder.getOrCreate();
    }
}
