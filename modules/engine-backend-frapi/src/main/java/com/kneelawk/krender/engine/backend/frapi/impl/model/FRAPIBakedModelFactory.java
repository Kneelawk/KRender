package com.kneelawk.krender.engine.backend.frapi.impl.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.model.BakedModelCore;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.backend.frapi.impl.FRAPIRenderer;

public class FRAPIBakedModelFactory implements BakedModelFactory {
    private boolean caching = true;

    @Override
    public BakedModelFactory setCaching(boolean caching) {
        this.caching = caching;
        return this;
    }

    @Override
    public boolean isCaching() {
        return caching;
    }

    @Override
    public @NotNull BakedModel wrap(@NotNull BakedModelCore<?> core) {
        if (caching) {
            throw new UnsupportedOperationException("Not implemented yet");
        } else {
            return new FRAPIBakedModelImpl(core);
        }
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return FRAPIRenderer.INSTNACE;
    }
}
