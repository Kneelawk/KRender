package com.kneelawk.krender.api.loading;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

/**
 * Implemented by users wishing to supply their own {@link UnbakedModel}s.
 */
@FunctionalInterface
public interface ModelLoader {
    /**
     * Loads model(s) associated with the given resource location.
     * <p>
     * When this method is called, the implementor should call {@link Context#putModel(ResourceLocation, UnbakedModel)}
     * with the requested model if the requested model is found. Additionally, the implementor can put extra models that
     * are likely to be needed alongside the given model.
     *
     * @param ctx the loading context, providing model location and accepting models.
     */
    void loadModels(Context ctx);

    /**
     * The model loading context.
     */
    @ApiStatus.NonExtendable
    interface Context {
        /**
         * The name of the requested model.
         *
         * @return the name of the requested model.
         */
        ResourceLocation location();

        /**
         * The {@link ModelBakery} that is loading the models.
         *
         * @return the model-bakery that requested the model.
         */
        ModelBakery bakery();

        /**
         * Puts a model into the model bakery.
         *
         * @param name  the name of the model to put.
         * @param model the model to put.
         */
        void putModel(ResourceLocation name, UnbakedModel model);

        /**
         * Use to retrieve an {@link UnbakedModel} from the underlying {@link ModelBakery}, potentially loading that
         * model if it has not been loaded already.
         *
         * @param name the name of the model to find or load.
         * @return the {@link UnbakedModel} for the given name.
         */
        UnbakedModel getOrLoadModel(ResourceLocation name);
    }
}
