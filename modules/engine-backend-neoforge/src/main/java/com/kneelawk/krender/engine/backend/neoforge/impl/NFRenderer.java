package com.kneelawk.krender.engine.backend.neoforge.impl;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.backend.neoforge.impl.material.NFRenderMaterial;
import com.kneelawk.krender.engine.backend.neoforge.impl.model.NFBakedModelFactory;
import com.kneelawk.krender.engine.base.material.BaseMaterialManager;

public class NFRenderer implements KRenderer {
    public static final NFRenderer INSTANCE = new NFRenderer();
    public static final BaseMaterialManager<NFRenderMaterial> MATERIAL_MANAGER =
        new BaseMaterialManager<>(INSTANCE, NFRenderMaterial::new);

    @Override
    public BakedModelFactory bakedModelFactory() {
        return NFBakedModelFactory.INSTANCE;
    }

    @Override
    public MaterialManager materialManager() {
        return MATERIAL_MANAGER;
    }

    @Override
    public String getName() {
        return KRBNFConstants.BACKEND_ID;
    }
}
