package com.kneelawk.krender.engine.backend.neoforge.impl.model;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.model.BakedModelCore;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;

public class NFBakedModelFactory implements BakedModelFactory {
    public static final NFBakedModelFactory INSTANCE = new NFBakedModelFactory();

    @Override
    public BakedModel wrap(BakedModelCore core) {
        return new NFBakedModelImpl(core);
    }
}
