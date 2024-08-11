package com.kneelawk.krender.model.creation.fabric.impl;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.model.creation.api.BakedModelCore;
import com.kneelawk.krender.model.creation.api.material.MaterialFinder;
import com.kneelawk.krender.model.creation.fabric.impl.material.FabricMaterialFinder;
import com.kneelawk.krender.model.creation.impl.Platform;

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
