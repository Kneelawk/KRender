package com.kneelawk.krender.engine.backend.neoforge.impl.model;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.model.BakedModelCore;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.backend.neoforge.impl.NFRenderer;

public class NFBakedModelFactory implements BakedModelFactory {
    @Override
    public BakedModel wrap(BakedModelCore core) {
        return new NFBakedModelImpl(core);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return NFRenderer.INSTANCE;
    }
}
