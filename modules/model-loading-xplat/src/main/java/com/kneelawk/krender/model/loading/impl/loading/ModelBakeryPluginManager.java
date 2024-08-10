package com.kneelawk.krender.model.loading.impl.loading;

import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import com.kneelawk.krender.model.loading.api.LowLevelModelProvider;
import com.kneelawk.krender.model.loading.impl.KRLog;
import com.kneelawk.krender.model.loading.impl.mixin.api.ModelBakeryHooks;

public class ModelBakeryPluginManager {
    public static final ThreadLocal<ModelBakeryPluginManager> CURRENT_MANAGER = new ThreadLocal<>();

    private final ModelBakery modelBakery;
    private final ModelBakeryHooks hooks;
    private final Map<ModelResourceLocation, ResourceLocation> extraTopLevelNames;
    private final Map<ModelResourceLocation, UnbakedModel> extraTopLevelModels;
    private final Map<ResourceLocation, UnbakedModel> extraLowLevelModels;
    private final LowLevelModelProvider[] lowLevelProviders;
    private final Map<ResourceLocation, BlockStateModelProviderHolder> blockStateProviders;

    public ModelBakeryPluginManager(ModelBakery modelBakery, ModelBakeryHooks hooks,
                                    Map<ModelResourceLocation, ResourceLocation> extraTopLevelNames,
                                    Map<ModelResourceLocation, UnbakedModel> extraTopLevelModels,
                                    Map<ResourceLocation, UnbakedModel> extraLowLevelModels,
                                    LowLevelModelProvider[] lowLevelProviders,
                                    Map<ResourceLocation, BlockStateModelProviderHolder> blockStateProviders) {
        this.modelBakery = modelBakery;
        this.hooks = hooks;
        this.extraTopLevelNames = extraTopLevelNames;
        this.extraTopLevelModels = extraTopLevelModels;
        this.extraLowLevelModels = extraLowLevelModels;
        this.lowLevelProviders = lowLevelProviders;
        this.blockStateProviders = blockStateProviders;
    }

    public void addExtraLowLevelModels(BiConsumer<ResourceLocation, UnbakedModel> adder) {
        extraLowLevelModels.forEach(adder);
    }

    public void addExtraTopLevelNames(BiConsumer<ModelResourceLocation, ResourceLocation> adder) {
        extraTopLevelNames.forEach(adder);
    }

    public void addExtraTopLevelModels(BiConsumer<ModelResourceLocation, UnbakedModel> adder) {
        extraTopLevelModels.forEach(adder);
    }

    public boolean loadBlockStateModels(ResourceLocation blockId, StateDefinition<Block, BlockState> states,
                                        BlockColors blockColors,
                                        BiConsumer<ModelResourceLocation, UnbakedModel> discoveredModelOutput) {
        BlockStateModelProviderHolder holder = blockStateProviders.get(blockId);
        if (holder != null) {
            return holder.loadBlockStateModels(blockId, states, blockColors, discoveredModelOutput, hooks);
        } else {
            return false;
        }
    }

    public UnbakedModel loadLowLevelModel(ResourceLocation resourceLocation) {
        LowLevelModelProviderContextImpl ctx = new LowLevelModelProviderContextImpl(resourceLocation, modelBakery);

        for (LowLevelModelProvider provider : lowLevelProviders) {
            try {
                UnbakedModel model = provider.getModel(ctx);
                if (model != null) return model;
            } catch (Exception e) {
                KRLog.LOGGER.error("Error getting model from custom model provider", e);
            }
        }

        return null;
    }
}
