package com.kneelawk.krender.api.loading;

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
     * <p>
     * This should not cause the additional loading of models from a resource-manager. All resources should already be
     * loaded during a preparation stage using {@link #registerPreparable(PreparableModelBakeryPlugin.ResourceLoader, PreparableModelBakeryPlugin)}.
     * See {@link net.minecraft.resources.FileToIdConverter} for efficient loading of all models that meet a certain
     * criteria.
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
         * Loads a model via default means (including via {@link LowLevelModelProvider}s) and then registers it as a top-level
         * model.
         * <p>
         * A top level model is one that can be obtained with {@code Minecraft.getInstance().getModelManager().getModel(name)}.
         * <p>
         * A block-state json is a good example of a top-level model.
         * <p>
         * Note: These models will be picked up by block-state model association.
         *
         * @param name the name of the model.
         * @param path where the model is actually found.
         */
        void addTopLevelModelName(ModelResourceLocation name, ResourceLocation path);

        /**
         * Loads a collection of models via default means (including via {@link LowLevelModelProvider}s) and then registers
         * them as top-level models.
         * <p>
         * A top level model is one that can be obtained with {@code Minecraft.getInstance().getModelManager().getModel(name)}.
         * <p>
         * A block-state json is a good example of a top-level model.
         * <p>
         * Note: These models will be picked up by block-state model association.
         *
         * @param models the model names and paths to load and add.
         */
        void addTopLevelModelNames(Map<ModelResourceLocation, ResourceLocation> models);

        /**
         * Adds an already loaded model to the {@link ModelBakery}'s set of top-level models to bake.
         * <p>
         * A top level model is one that can be obtained with {@code Minecraft.getInstance().getModelManager().getModel(name)}.
         * <p>
         * A block-state json is a good example of a top-level model.
         * <p>
         * Note: These models will be picked up by block-state model association.
         *
         * @param name  the name of the model.
         * @param model the model to bake.
         */
        void addTopLevelModel(ModelResourceLocation name, UnbakedModel model);

        /**
         * Adds a collection of already loaded models to the {@link ModelBakery}'s set of models to bake.
         * <p>
         * A top level model is one that can be obtained with {@code Minecraft.getInstance().getModelManager().getModel(name)}.
         * <p>
         * A block-state json is a good example of a top-level model.
         * <p>
         * Note: These models will be picked up by block-state model association.
         *
         * @param models the models to bake.
         */
        void addTopLevelModels(Map<ModelResourceLocation, ? extends UnbakedModel> models);

        /**
         * Adds an already loaded model to the {@link ModelBakery}'s set of lower-level models.
         * <p>
         * These models cannot be accessed directly, but can be referenced by a top-level model and used that way.
         *
         * @param path  the path to associate the model with.
         * @param model the model to add.
         */
        void addLowLevelModel(ResourceLocation path, UnbakedModel model);

        /**
         * Adds already loaded models to the {@link ModelBakery}'s set of lower-level models.
         * <p>
         * These models cannot be accessed directly, but can be referenced by a top-level model and used that way.
         *
         * @param models the models to make available to top-level models.
         */
        void addLowLevelModels(Map<ResourceLocation, ? extends UnbakedModel> models);

        /**
         * Registers a custom lower-level model provider.
         * <p>
         * These models are the ones that are typically referenced by block-state models.
         * <p>
         * These models are also the ones used by items.
         *
         * @param provider the provider to register.
         */
        void registerLowLevelModelProvider(LowLevelModelProvider provider);

        /**
         * Registers a custom block-state model provider.
         * <p>
         * These models are ones that can be obtained via {@code Minecraft.getInstance().getModelManager().getModel(name)}.
         * <p>
         * This provides more control than registering models via the {@link #addTopLevelModel(ModelResourceLocation, UnbakedModel)}
         * set of methods.
         *
         * @param provider the provider to register.
         */
        void registerBlockStateModelProvider(BlockStateModelProvider provider);
    }
}
