package com.kneelawk.krender.api.loading;

import java.util.Collection;
import java.util.Map;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.impl.loading.ModelBakeryPluginRegistrar;

/**
 * Allows hooking into the mechanism of the {@link ModelBakery} when it is loading.
 */
@FunctionalInterface
public interface ModelBakeryPlugin {
    /**
     * Use to register a simple {@link ModelBakeryPlugin}.
     *
     * @param plugin the plugin to register.
     */
    static void register(ModelBakeryPlugin plugin) {
        ModelBakeryPluginRegistrar.register(plugin);
    }

    /**
     * Used to register a {@link PreparableModelBakeryPlugin} that loads resources before registering objects.
     *
     * @param loader the resource loader.
     * @param plugin the plugin that registers objects.
     * @param <T>    the type of resource that the plugin loads.
     */
    static <T> void registerPreparable(PreparableModelBakeryPlugin.ResourceLoader<T> loader,
                                       PreparableModelBakeryPlugin<T> plugin) {
        ModelBakeryPluginRegistrar.registerPreparable(loader, plugin);
    }

    /**
     * Initialize the model bakery plugin by registering objects with the given context.
     * <p>
     * This may be called multiple times.
     *
     * @param ctx the initialization context used to register various functions.
     */
    void init(Context ctx);

    /**
     * The context supplied to a loaded {@link ModelBakeryPlugin}.
     */
    @ApiStatus.NonExtendable
    interface Context {
        /**
         * Adds models by their {@link ResourceLocation}s or {@link ModelResourceLocation}s to the {@link ModelBakery}'s
         * set of models to load and bake.
         *
         * @param names the names of models to load and bake.
         */
        void addModels(ModelResourceLocation... names);

        /**
         * Adds a collection of models by their {@link ResourceLocation}s or {@link ModelResourceLocation}s to the
         * {@link ModelBakery}'s set of models to load and bake.
         *
         * @param names the names of models to load and bake.
         */
        void addModels(Collection<? extends ModelResourceLocation> names);

        /**
         * Adds an already loaded model to the {@link ModelBakery}'s set of models to bake.
         *
         * @param name  the name of the model.
         * @param model the model to bake.
         */
        void addModel(ModelResourceLocation name, UnbakedModel model);

        /**
         * Adds a collection of already loaded models to the {@link ModelBakery}'s set of models to bake.
         *
         * @param models the models to bake.
         */
        void addModels(Map<? extends ModelResourceLocation, ? extends UnbakedModel> models);

        /**
         * Registers a custom model loader.
         * <p>
         * Custom model loaders allow more control over how a model is loaded, but they are also slower than supplying
         * models ahead of time.
         *
         * @param loader the loader to register.
         */
        void registerModelLoader(ModelProvider loader);
    }
}
