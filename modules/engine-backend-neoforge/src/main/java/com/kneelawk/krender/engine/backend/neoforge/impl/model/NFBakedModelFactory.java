package com.kneelawk.krender.engine.backend.neoforge.impl.model;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.model.BlockStateModelCore;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.backend.neoforge.impl.NFRenderer;

public class NFBakedModelFactory implements BakedModelFactory {
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
    public BakedModel wrap(BlockStateModelCore core) {
        if (caching) {
            return new NFCachingBakedModelImpl(core);
        } else {
            return new NFBakedModelImpl(core);
        }
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return NFRenderer.INSTANCE;
    }
}
