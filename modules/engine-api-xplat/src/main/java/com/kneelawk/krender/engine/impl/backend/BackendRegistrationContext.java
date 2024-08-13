package com.kneelawk.krender.engine.impl.backend;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.backend.BackendRegistrationCallback;
import com.kneelawk.krender.engine.api.backend.KRenderBackend;

public class BackendRegistrationContext implements BackendRegistrationCallback.Context {
    private final Map<String, KRenderer> renderers = new LinkedHashMap<>();
    private @Nullable String best = null;
    private int bestPriority = Integer.MAX_VALUE;

    @Override
    public void registerBackend(@NotNull KRenderBackend backend) {
        Objects.requireNonNull(backend, "backend should never be null");

        if (renderers.containsKey(backend.getName())) {
            throw new IllegalStateException(
                "Two renderers are present with the same name: " + backend.getName() + " (" +
                    renderers.get(backend.getName()).getClass() + " and " + backend.getRenderer().getClass() + ")");
        }

        if (best == null || backend.getPriority() < bestPriority) {
            best = backend.getName();
            bestPriority = backend.getPriority();
        }

        renderers.put(backend.getName(), backend.getRenderer());
    }

    public Map<String, KRenderer> getRenderers() {
        return renderers;
    }

    public @Nullable String getBest() {
        return best;
    }
}
