package com.kneelawk.krender.api.loading;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

/**
 * Implemented by users wishing to supply their own {@link UnbakedModel}s.
 */
@FunctionalInterface
public interface ModelProvider {
    /**
     * Loads a model associated with the given resource location.
     *
     * @param ctx the loading context, providing model location and accepting models.
     * @return the model for the given 
     */
    @Nullable UnbakedModel loadModel(Context ctx);

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
    }
}
