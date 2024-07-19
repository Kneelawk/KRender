package com.kneelawk.krender.impl.loading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.server.packs.resources.ResourceManager;

import com.kneelawk.krender.api.loading.ModelBakeryPlugin;
import com.kneelawk.krender.api.loading.ModelBakeryPluginCallback;
import com.kneelawk.krender.api.loading.PreparableModelBakeryPlugin;
import com.kneelawk.krender.impl.KRLog;

public class ModelBakeryPluginRegistrar implements ModelBakeryPluginCallback.Context {
    public static final ModelBakeryPluginRegistrar pluginRegistrar = new ModelBakeryPluginRegistrar();
    // reload is only executed once at a time, usually on the render thread, so this should be safe
    public static AtomicReference<CompletableFuture<PreparedModelBakeryPluginList>> PREPARE_FUTURE =
        new AtomicReference<>();

    public static void preparePlugins(ResourceManager resourceManager, Executor prepareExecutor) {
        PREPARE_FUTURE.set(pluginRegistrar.prepare(resourceManager, prepareExecutor));
    }

    private final List<ModelBakeryPlugin> plugins = new ArrayList<>();
    private final List<PreparableModelBakeryPluginHolder<?>> preparables = new ArrayList<>();

    public ModelBakeryPluginRegistrar() {
        ModelBakeryPluginCallback.EVENT.invoker().modelBakeryLoading(this);
    }

    public CompletableFuture<PreparedModelBakeryPluginList> prepare(ResourceManager resourceManager,
                                                                    Executor prepareExecutor) {
        List<CompletableFuture<? extends PreparedModelBakeryPlugin<?>>> preparedList = new ArrayList<>();
        for (PreparableModelBakeryPluginHolder<?> preparable : preparables) {
            try {
                CompletableFuture<? extends PreparedModelBakeryPlugin<?>> load =
                    preparable.load(resourceManager, prepareExecutor).handle((prepared, ex) -> {
                        if (ex == null) {
                            return prepared;
                        } else {
                            KRLog.LOGGER.error("Error preparing plugin", ex);
                            return null;
                        }
                    });
                preparedList.add(load);
            } catch (Exception e) {
                KRLog.LOGGER.error("Error starting plugin preparation", e);
            }
        }

        CompletableFuture<Void> preparedAll = CompletableFuture.allOf(preparedList.toArray(CompletableFuture[]::new));

        return preparedAll.thenApply(void_ -> {
            List<PreparedModelBakeryPlugin<?>> list = new ArrayList<>();
            for (CompletableFuture<? extends PreparedModelBakeryPlugin<?>> preparedModelBakeryPluginCompletableFuture : preparedList) {
                try {
                    PreparedModelBakeryPlugin<?> plugin = preparedModelBakeryPluginCompletableFuture.get();
                    if (plugin != null) list.add(plugin);
                } catch (Exception e) {
                    KRLog.LOGGER.error("Error preparing plugin", e);
                }
            }
            return new PreparedModelBakeryPluginList(plugins, list);
        });
    }

    @Override
    public void register(ModelBakeryPlugin plugin) {
        plugins.add(plugin);
    }

    @Override
    public <T> void registerPreparable(PreparableModelBakeryPlugin.ResourceLoader<T> loader,
                                       PreparableModelBakeryPlugin<T> plugin) {
        preparables.add(new PreparableModelBakeryPluginHolder<>(loader, plugin));
    }
}
