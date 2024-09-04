package com.kneelawk.krender.engine.backend.frapi.impl;

import org.jetbrains.annotations.NotNull;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.backend.KRenderBackend;

public class FRAPIBackend implements KRenderBackend {
    @Override
    public @NotNull KRenderer getRenderer() {
        return FRAPIRenderer.INSTNACE;
    }

    @Override
    public @NotNull String getName() {
        return KRBFRConstants.BACKEND_ID;
    }

    @Override
    public int getPriority() {
        return 950;
    }
}
