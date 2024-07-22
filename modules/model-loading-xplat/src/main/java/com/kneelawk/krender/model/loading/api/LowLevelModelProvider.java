package com.kneelawk.krender.model.loading.api;

import java.util.function.Function;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

/**
 * Implemented by users wishing to supply their own {@link UnbakedModel}s.
 * <p>
 * Use {@link ModelBakeryPlugin.Context#registerLowLevelModelProvider(LowLevelModelProvider)} to register an
 * implementor of this interface.
 */
@FunctionalInterface
public interface LowLevelModelProvider {
    /**
     * Loads a model associated with the given resource location.
     * <p>
     * Note: this should never call {@link ModelBakery#getModel(ResourceLocation)}. Instead, models used in a model
     * being loaded should be obtained during {@link UnbakedModel#resolveParents(Function)} or during
     * .{@link UnbakedModel#bake(ModelBaker, Function, ModelState)} via {@link ModelBaker#bake(ResourceLocation, ModelState)}/
     *
     * @param ctx the loading context, providing model location and accepting models.
     * @return the model for the given model location.
     */
    @Nullable
    UnbakedModel getModel(Context ctx);

    /**
     * The low-level model provider context.
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
