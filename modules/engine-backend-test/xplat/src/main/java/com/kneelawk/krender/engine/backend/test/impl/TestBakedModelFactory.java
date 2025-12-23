package com.kneelawk.krender.engine.backend.test.impl;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.model.BakedModelCore;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;

public class TestBakedModelFactory implements BakedModelFactory {
    // does nothing in the test impl
    private boolean caching = true;

    @Override
    public @NotNull BakedModelFactory setCaching(boolean caching) {
        this.caching = caching;
        return this;
    }

    @Override
    public boolean isCaching() {
        return caching;
    }

    @Override
    public @NotNull BakedModel wrap(BakedModelCore<?> core) {
        return new TestBakedModel(core);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return TestRenderer.INSTANCE;
    }
}
