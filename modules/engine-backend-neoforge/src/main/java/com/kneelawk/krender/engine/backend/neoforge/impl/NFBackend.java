package com.kneelawk.krender.engine.backend.neoforge.impl;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.backend.KRenderBackend;

public class NFBackend implements KRenderBackend {
    public static final NFBackend INSTANCE = new NFBackend();

    @Override
    public KRenderer getRenderer() {
        return NFRenderer.INSTANCE;
    }

    @Override
    public String getName() {
        return "neoforge";
    }

    @Override
    public int getPriority() {
        return 1000;
    }
}
