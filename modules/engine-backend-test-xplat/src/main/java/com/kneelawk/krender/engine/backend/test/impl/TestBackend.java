package com.kneelawk.krender.engine.backend.test.impl;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.backend.KRenderBackend;

public class TestBackend implements KRenderBackend {
    public static final TestBackend INSTANCE = new TestBackend();

    @Override
    public KRenderer getRenderer() {
        return TestRenderer.INSTANCE;
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public int getPriority() {
        return 1500;
    }
}
