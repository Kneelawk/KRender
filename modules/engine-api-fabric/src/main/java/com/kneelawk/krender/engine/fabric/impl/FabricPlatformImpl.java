package com.kneelawk.krender.engine.fabric.impl;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.BakedModelCore;
import com.kneelawk.krender.engine.api.material.MaterialFinder;
import com.kneelawk.krender.engine.fabric.impl.material.FabricMaterialFinder;
import com.kneelawk.krender.engine.impl.Platform;

public class FabricPlatformImpl implements Platform {
    @Override
    public BakedModel wrap(BakedModelCore core) {
        return new FabricBakedModelImpl(core);
    }

    @Override
    public MaterialFinder getOrCreateMaterialFinder() {
        return FabricMaterialFinder.getOrCreate();
    }
}
