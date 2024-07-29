package com.kneelawk.krender.model.creation.neoforge.impl;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.model.creation.api.BakedModelCore;
import com.kneelawk.krender.model.creation.api.material.MaterialFinder;
import com.kneelawk.krender.model.creation.impl.Platform;
import com.kneelawk.krender.model.creation.neoforge.impl.material.NeoForgeMaterialFinder;

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
