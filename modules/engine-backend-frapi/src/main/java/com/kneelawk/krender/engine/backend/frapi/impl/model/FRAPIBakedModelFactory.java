package com.kneelawk.krender.engine.backend.frapi.impl.model;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.engine.api.model.BakedModelCore;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;

public class FRAPIBakedModelFactory implements BakedModelFactory {
    public static final FRAPIBakedModelFactory INSTANCE = new FRAPIBakedModelFactory();

    @Override
    public @NotNull BakedModel wrap(@NotNull BakedModelCore core) {
        return new FRAPIBakedModelImpl(core);
    }
}
