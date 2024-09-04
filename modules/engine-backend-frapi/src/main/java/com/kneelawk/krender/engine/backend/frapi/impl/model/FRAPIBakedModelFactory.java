package com.kneelawk.krender.engine.backend.frapi.impl.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.model.BakedModelCore;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;
import com.kneelawk.krender.engine.backend.frapi.impl.FRAPIRenderer;

public class FRAPIBakedModelFactory implements BakedModelFactory {
    public static final FRAPIBakedModelFactory INSTANCE = new FRAPIBakedModelFactory();

    @Override
    public @NotNull BakedModel wrap(@NotNull BakedModelCore core) {
        return new FRAPIBakedModelImpl(core);
    }

    @Override
    public @Nullable KRenderer getRenderer() {
        return FRAPIRenderer.INSTNACE;
    }
}
