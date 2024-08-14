package com.kneelawk.krender.engine.backend.frapi.impl.material;

import com.kneelawk.krender.engine.api.material.MaterialFinder;
import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.material.RenderMaterial;

public class FRAPIMaterialManager implements MaterialManager {
    public static final FRAPIMaterialManager INSTANCE = new FRAPIMaterialManager();

    @Override
    public MaterialFinder materialFinder() {
        return FRAPIMaterialFinder.getOrCreate();
    }

    @Override
    public RenderMaterial defaultMaterial() {
        return FRAPIRenderMaterial.DEFAULT;
    }

    @Override
    public RenderMaterial missingMaterial() {
        return FRAPIRenderMaterial.DEFAULT;
    }
}
