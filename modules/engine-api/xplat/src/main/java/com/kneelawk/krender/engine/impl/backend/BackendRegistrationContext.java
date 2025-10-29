package com.kneelawk.krender.engine.impl.backend;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.kneelawk.krender.engine.api.backend.BackendRegistrationCallback;
import com.kneelawk.krender.engine.api.backend.KRenderBackend;

public class BackendRegistrationContext implements BackendRegistrationCallback.Context {
    private final Map<String, KRenderBackend> renderers = new LinkedHashMap<>();

    @Override
    public void registerBackend(@NotNull KRenderBackend backend) {
        Objects.requireNonNull(backend, "backend should never be null");

        if (renderers.containsKey(backend.getName())) {
            throw new IllegalStateException(
                "Two renderers are present with the same name: " + backend.getName() + " (" +
                    renderers.get(backend.getName()).getClass() + " and " + backend.getRenderer().getClass() + ")");
        }

        renderers.put(backend.getName(), backend);
    }

    public Map<String, KRenderBackend> getBackends() {
        return renderers;
    }
}
