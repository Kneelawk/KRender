package com.kneelawk.krender.model.loading.impl.loading;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import com.kneelawk.krender.model.loading.api.BlockStateModelProvider;
import com.kneelawk.krender.model.loading.impl.KRLog;
import com.kneelawk.krender.model.loading.impl.mixin.api.ModelBakeryHooks;

public record BlockStateModelProviderHolder(Map<ModelResourceLocation, ResourceLocation> extraTopLevelNames,
                                            Map<ModelResourceLocation, UnbakedModel> extraTopLevelModels,
                                            BlockStateModelProvider[] blockStateProviders) {
    public boolean loadBlockStateModels(ResourceLocation blockId, StateDefinition<Block, BlockState> states,
                                        BlockColors blockColors,
                                        BiConsumer<ModelResourceLocation, UnbakedModel> discoveredModelOutput,
                                        ModelBakeryHooks hooks) {
        HashSet<ModelResourceLocation> missingModels = new LinkedHashSet<>(
            states.getPossibleStates().stream().map(state -> BlockModelShaper.stateToModelLocation(blockId, state))
                .toList());
        int initialSize = missingModels.size();

        for (Iterator<ModelResourceLocation> iter = missingModels.iterator(); iter.hasNext(); ) {
            ModelResourceLocation name = iter.next();

            if (extraTopLevelModels.containsKey(name)) {
                UnbakedModel model = extraTopLevelModels.get(name);
                discoveredModelOutput.accept(name, model);
                iter.remove();
            } else if (extraTopLevelNames.containsKey(name)) {
                UnbakedModel model = hooks.krender$getOrLoadModel(extraTopLevelNames.get(name));
                discoveredModelOutput.accept(name, model);
                iter.remove();
            }
        }

        if (!missingModels.isEmpty()) {
            BlockStateModelProviderContextImpl ctx =
                new BlockStateModelProviderContextImpl(hooks, missingModels, blockId, states, blockColors,
                    discoveredModelOutput);

            for (BlockStateModelProvider provider : blockStateProviders) {
                if (missingModels.isEmpty()) break;

                try {
                    provider.loadModels(ctx);
                } catch (Exception e) {
                    KRLog.LOGGER.error("Error loading models from custom block-state model provider", e);
                }
            }
        }

        return missingModels.size() < initialSize;
    }
}
