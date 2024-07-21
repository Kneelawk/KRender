package com.kneelawk.krender.impl.loading;

import java.util.Set;
import java.util.function.BiConsumer;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import com.kneelawk.krender.api.loading.BlockStateModelProvider;
import com.kneelawk.krender.impl.mixin.api.ModelBakeryHooks;

public class BlockStateModelProviderContextImpl implements BlockStateModelProvider.Context {
    private final ModelBakeryHooks hooks;
    private final Set<ModelResourceLocation> missingModels;
    private final ResourceLocation blockId;
    private final StateDefinition<Block, BlockState> states;
    private final BlockColors blockColors;
    private final BiConsumer<ModelResourceLocation, UnbakedModel> discoveredModelOutput;

    public BlockStateModelProviderContextImpl(ModelBakeryHooks hooks, Set<ModelResourceLocation> missingModels,
                                              ResourceLocation blockId, StateDefinition<Block, BlockState> states,
                                              BlockColors blockColors,
                                              BiConsumer<ModelResourceLocation, UnbakedModel> discoveredModelOutput) {
        this.hooks = hooks;
        this.missingModels = missingModels;
        this.blockId = blockId;
        this.states = states;
        this.blockColors = blockColors;
        this.discoveredModelOutput = discoveredModelOutput;
    }


    @Override
    public ResourceLocation blockId() {
        return blockId;
    }

    @Override
    public StateDefinition<Block, BlockState> states() {
        return states;
    }

    @Override
    public BlockColors blockColors() {
        return blockColors;
    }

    @Override
    public boolean modelExists(ModelResourceLocation name) {
        return hooks.krender$topLevelModelExists(name);
    }

    @Override
    public UnbakedModel getOrLoadModel(ResourceLocation path) {
        return hooks.krender$getOrLoadModel(path);
    }

    @Override
    public void addTopLevelModel(ModelResourceLocation name, UnbakedModel model) {
        missingModels.remove(name);
        discoveredModelOutput.accept(name, model);
    }
}
