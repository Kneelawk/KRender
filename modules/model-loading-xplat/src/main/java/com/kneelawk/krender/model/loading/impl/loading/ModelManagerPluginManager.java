package com.kneelawk.krender.model.loading.impl.loading;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelDiscovery;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.SpriteGetter;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import com.kneelawk.krender.model.loading.impl.mixin.api.Duck_ModelBakeryBakingResult;

public class ModelManagerPluginManager {
    public static final ThreadLocal<ModelManagerPluginManager> CURRENT_MANAGER = new ThreadLocal<>();

    private final Map<ResourceLocation, UnbakedModel> referenceableModels;
    private final Map<BlockState, BlockStateModel.UnbakedRoot> blockStateModels;
    private final Set<ResourceLocation> extraModels;

    public ModelManagerPluginManager(Map<ResourceLocation, UnbakedModel> referenceableModels,
                                     Map<BlockState, BlockStateModel.UnbakedRoot> blockStateModels,
                                     Set<ResourceLocation> extraModels) {
        this.referenceableModels = referenceableModels;
        this.blockStateModels = blockStateModels;
        this.extraModels = extraModels;
    }

    public Map<ResourceLocation, UnbakedModel> addReferenceableModels(Map<ResourceLocation, UnbakedModel> models) {
        if (!(models instanceof HashMap<ResourceLocation, UnbakedModel>)) {
            models = new LinkedHashMap<>(models);
        }
        models.putAll(referenceableModels);
        return models;
    }

    public BlockStateModelLoader.LoadedModels addBlockStateModels(BlockStateModelLoader.LoadedModels models) {
        if (!(models.models() instanceof HashMap<BlockState, BlockStateModel.UnbakedRoot>)) {
            models = new BlockStateModelLoader.LoadedModels(new LinkedHashMap<>(models.models()));
        }

        Map<BlockState, BlockStateModel.UnbakedRoot> map = models.models();
        map.putAll(blockStateModels);

        return models;
    }

    public void resolveExtraModels(ModelDiscovery modelDiscovery,
                                   Function<ResourceLocation, ModelDiscovery.ModelWrapper> getBlockModel) {
        for (ResourceLocation extraModelPath : extraModels) {
            modelDiscovery.addSpecialModel(extraModelPath, getBlockModel.apply(extraModelPath).wrapped());
        }
    }

    public void bakeExtraModels(ModelBakery bakery, SpriteGetter textureGetter,
                                CompletableFuture<ModelBakery.BakingResult> bakingResult) {
        Map<ResourceLocation, BlockStateModel> extraBakedModels = new Object2ObjectLinkedOpenHashMap<>();
        for (ResourceLocation extraModelPath : extraModels) {
            ModelBaker.SharedOperationKey<BlockStateModel> cachedKey = (modelBaker) ->
                new SingleVariant(SimpleModelWrapper.bake(modelBaker, extraModelPath, BlockModelRotation.X0_Y0));
            BlockStateModel baked =
                bakery.new ModelBakerImpl(textureGetter).compute(cachedKey);
            extraBakedModels.put(extraModelPath, baked);
        }
        bakingResult.thenApplyAsync(bakingResult1 -> {
            ((Duck_ModelBakeryBakingResult) (Object) bakingResult1).krender$setExtraModels(extraBakedModels);
            return bakingResult1;
        });
    }
}
