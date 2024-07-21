package com.kneelawk.krender.api.loading;

import java.util.function.Function;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

/**
 * Implemented by users wishing to supply their own block-state {@link UnbakedModel}.
 */
@FunctionalInterface
public interface BlockStateModelProvider {
    /**
     * Loads all models associated with the given block.
     * <p>
     * This will only be called if not all the block-states for the given block have had top-level models added for
     * them previously, either by a model-bakery plugin or by a previous block-state model provider.
     * <p>
     * Note: it is safe to call {@link ModelBakery#getModel(ResourceLocation)} here, and
     * {@link Context#getOrLoadModel(ResourceLocation)} is available for this purpose. However, if the model being
     * loaded is not the same model as the one being added and is instead only a dependency of the one being added, it
     * is often preferable that the dependent model instead load the dependency model during
     * {@link UnbakedModel#resolveParents(Function)} or during {@link UnbakedModel#bake(ModelBaker, Function, ModelState)}
     * via {@link ModelBaker#bake(ResourceLocation, ModelState)}.
     *
     * @param ctx the provider context, both supplying info about the block-states being loaded and providing a means
     *            to register models for those block-states.
     */
    void loadModels(Context ctx);

    /**
     * The block-state model provider context.
     */
    @ApiStatus.NonExtendable
    interface Context {
        /**
         * {@return the id of the block for which all models are being loaded}
         */
        ResourceLocation blockId();

        /**
         * {@return the set of block-states for the block being loaded}
         */
        StateDefinition<Block, BlockState> states();

        /**
         * {@return the model loader's set of block colors}
         */
        BlockColors blockColors();

        /**
         * Checks whether the given top-level model exists.
         *
         * @param name the name of the top-level model to check.
         * @return whether the given top-level model exists.
         */
        boolean modelExists(ModelResourceLocation name);

        /**
         * Loads an available model.
         * <p>
         * Note: available models are ones that have already been picked up by Minecraft's model-loading mechanism by
         * being in the {@code models/} asset folder, or that are supplied by model bakery plugins, like via a
         * {@link LowLevelModelProvider}.
         *
         * @param path the resource path to load the model from. Note: this omits the {@code models/} prefix.
         * @return the unbaked model with the given path.
         */
        UnbakedModel getOrLoadModel(ResourceLocation path);

        /**
         * Adds a model with the given name as a top-level model to be baked.
         *
         * @param name  the name of the model. This can be obtained via {@link BlockModelShaper#stateToModelLocation(ResourceLocation, BlockState)}.
         * @param model the model to add.
         */
        void addTopLevelModel(ModelResourceLocation name, UnbakedModel model);
    }
}
