package com.kneelawk.krender.engine.backend.neoforge.impl;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.backend.neoforge.impl.model.NFBakedModelFactory;

public class NFRenderer implements KRenderer {
    public static final NFRenderer INSTANCE = new NFRenderer();

    @Override
    public BakedModelFactory bakedModelFactory() {
        return NFBakedModelFactory.INSTANCE;
    }

    @Override
    public MaterialManager materialManager() {
        throw new RuntimeException("Not yet implemented");
    }
}
