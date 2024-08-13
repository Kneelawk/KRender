package com.kneelawk.krender.engine.impl.backend;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.kneelawk.krender.engine.api.backend.BackendRegistrationCallback;
import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.impl.KRELog;

public class BackendManager {
    private static final Map<String, KRenderer> renderers;
    private static final String bestRenderer;
    private static KRenderer defaultRenderer;

    static {
        KRELog.LOG.info("Loading KRender Engine backends...");
        BackendRegistrationContext ctx = new BackendRegistrationContext();
        BackendRegistrationCallback.EVENT.invoker().registerBackends(ctx);
        renderers = ctx.getRenderers();
        bestRenderer = ctx.getBest();

        if (renderers.isEmpty()) {
            KRELog.LOG.error("No KRender Engine backends present! KRender Engine will not work.");
        } else {
            KRELog.LOG.info("Best KRender Engine backend: {}", bestRenderer);

            String defaultName = BackendConfig.getDefaultBackend(bestRenderer, renderers.keySet());
            defaultRenderer = renderers.get(defaultName);
            KRELog.LOG.info("Default KRender Engine backend: {}", defaultName);

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
        KRenderer renderer = renderers.get(name);
        if (renderer == null) throw new RuntimeException("No KRender Engine backend with name: " + name);
        return renderer;
    }

    public static @Nullable KRenderer tryGet(String name) {
        return renderers.get(name);
    }
}
