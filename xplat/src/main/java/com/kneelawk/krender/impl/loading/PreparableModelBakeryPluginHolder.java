package com.kneelawk.krender.impl.loading;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.server.packs.resources.ResourceManager;

import com.kneelawk.krender.api.loading.PreparableModelBakeryPlugin;

public record PreparableModelBakeryPluginHolder<T>(PreparableModelBakeryPlugin.ResourceLoader<T> loader,
                                                   PreparableModelBakeryPlugin<T> plugin) {
    CompletableFuture<PreparedModelBakeryPlugin<T>> load(ResourceManager resourceManager,
                                                         Executor prepareExecutor) {
        return loader.loadResource(resourceManager, prepareExecutor)
            .thenApply(resource -> new PreparedModelBakeryPlugin<>(resource, plugin));
    }
}
