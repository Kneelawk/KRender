package com.kneelawk.krender.engine.impl.backend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.jetbrains.annotations.Nullable;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.backend.BackendRegistrationCallback;
import com.kneelawk.krender.engine.api.backend.KRenderBackend;
import com.kneelawk.krender.engine.impl.KRELog;

public class BackendManager {
    private static final Map<String, KRenderer> renderersByName;
    private static final List<KRenderer> renderersByPriority;
    private static volatile KRenderer defaultRenderer;

    static {
        KRELog.LOG.info("Loading KRender Engine backends...");
        BackendRegistrationContext ctx = new BackendRegistrationContext();
        BackendRegistrationCallback.EVENT.invoker().registerBackends(ctx);
        Map<String, KRenderBackend> backends = ctx.getBackends();
        Map<String, Integer> priorities = backends.entrySet().stream()
            .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, e -> e.getValue().getPriority()));
        renderersByName = backends.entrySet().stream()
            .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, e -> e.getValue().getRenderer()));

        if (backends.isEmpty()) {
            KRELog.LOG.error("No KRender Engine backends present! KRender Engine will not work.");
            renderersByPriority = List.of();
        } else {
            String bestRenderer = priorities.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
            KRELog.LOG.info("Best KRender Engine backend: {}", bestRenderer);

            Map<String, Integer> newPriorities = BackendConfig.getDefaultBackend(priorities);
            ArrayList<String> sortedBackends = new ArrayList<>(backends.keySet());
            sortedBackends.sort(Comparator.comparing(newPriorities::get));
            String defaultName = sortedBackends.getFirst();

            KRELog.LOG.info("Default KRender Engine backend: {}", defaultName);

            renderersByPriority =
                sortedBackends.stream().map(renderersByName::get).collect(ImmutableList.toImmutableList());
            defaultRenderer = renderersByPriority.getFirst();

            KRELog.LOG.info("KRender Engine backends loaded.");
        }
    }

    public static KRenderer getDefault() {
        KRenderer defaultRenderer = BackendManager.defaultRenderer;
        if (defaultRenderer == null) throw new RuntimeException("No KRender Engine backends are present");
        return defaultRenderer;
    }

    public static @Nullable KRenderer tryGetDefault() {
        return defaultRenderer;
    }

    public static KRenderer get(String name) {
        KRenderer renderer = renderersByName.get(name);
        if (renderer == null) throw new RuntimeException("No KRender Engine backend with name: " + name);
        return renderer;
    }

    public static @Nullable KRenderer tryGet(String name) {
        return renderersByName.get(name);
    }
}
