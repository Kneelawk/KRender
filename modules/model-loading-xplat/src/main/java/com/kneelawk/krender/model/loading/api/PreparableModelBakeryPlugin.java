package com.kneelawk.krender.model.loading.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.server.packs.resources.ResourceManager;

/**
 * Allows hooking into the mechanism of the {@link net.minecraft.client.resources.model.ModelBakery} when it is loading.
 * <p>
 * Unlike the {@link ModelBakeryPlugin}, this plugin allows resources to be loaded beforehand during the loading stage.
 *
 * @param <T> the type of resource that must be prepared for this plugin.
 */
@FunctionalInterface
public interface PreparableModelBakeryPlugin<T> {

    /**
     * Initialize the model bakery plugin by registering objects with the given context.
     * <p>
     * This may be called multiple times.
     *
     * @param resource the resource loaded via {@link ResourceLoader#loadResource(ResourceManager, Executor)}.
     * @param ctx      the context, used for registering objects with the model bakery.
     */
    void init(T resource, ModelBakeryPlugin.Context ctx);

    /**
     * Loads a resource, preparing the {@link PreparableModelBakeryPlugin}.
     *
     * @param <T> the type of resource to be loaded.
     */
    @FunctionalInterface
    interface ResourceLoader<T> {
        /**
         * Loads a resource, preparing the {@link PreparableModelBakeryPlugin}.
         * <p>
         * This is called every time the {@link net.minecraft.client.resources.model.ModelBakery} loads so that the
         * plugin can detect changes made to resources since the last reload.
         *
         * @param resourceManager the resource manager that supplies resources.
         * @param prepareExecutor the executor for the thread pool that resource loading must take place on.
         * @return a future that loads the resource.
         */
        CompletableFuture<T> loadResource(ResourceManager resourceManager, Executor prepareExecutor);
    }
}
