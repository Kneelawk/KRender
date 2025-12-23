package com.kneelawk.krender.model.loading.impl.loading;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.UnbakedBlockStateModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ModelDiscovery;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.client.resources.model.SpriteGetter;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

import com.kneelawk.krender.model.loading.impl.mixin.api.Duck_ModelBakeryBakingResult;

public class ModelManagerPluginManager {
    public static final ThreadLocal<ModelManagerPluginManager> CURRENT_MANAGER = new ThreadLocal<>();

    private final Map<Identifier, UnbakedModel> referenceableModels;
    private final Map<BlockState, UnbakedBlockStateModel> blockStateModels;
    private final Set<Identifier> extraModels;

    public ModelManagerPluginManager(Map<Identifier, UnbakedModel> referenceableModels,
                                     Map<BlockState, UnbakedBlockStateModel> blockStateModels,
                                     Set<Identifier> extraModels) {
        this.referenceableModels = referenceableModels;
        this.blockStateModels = blockStateModels;
        this.extraModels = extraModels;
    }

    public Map<Identifier, UnbakedModel> addReferenceableModels(Map<Identifier, UnbakedModel> models) {
        if (!(models instanceof HashMap<Identifier, UnbakedModel>)) {
            models = new LinkedHashMap<>(models);
        }
        models.putAll(referenceableModels);
        return models;
    }

    public BlockStateModelLoader.LoadedModels addBlockStateModels(BlockStateModelLoader.LoadedModels models) {
        if (!(models.models() instanceof HashMap<ModelIdentifier, BlockStateModelLoader.LoadedModel>)) {
            models = new BlockStateModelLoader.LoadedModels(new LinkedHashMap<>(models.models()));
        }

        Map<ModelIdentifier, BlockStateModelLoader.LoadedModel> map = models.models();
        for (var entry : blockStateModels.entrySet()) {
            map.put(BlockModelShaper.stateToModelLocation(entry.getKey()),
                new BlockStateModelLoader.LoadedModel(entry.getKey(), entry.getValue()));
        }

        return models;
    }

    public void resolveExtraModels(ModelDiscovery modelDiscovery,
                                   Function<Identifier, UnbakedModel> getBlockModel) {
        for (Identifier extraModelPath : extraModels) {
            getBlockModel.apply(extraModelPath).resolveDependencies(modelDiscovery.new ResolverImpl());
        }
    }

    public void bakeExtraModels(ModelBakery bakery, SpriteGetter textureGetter,
                                ModelBakery.BakingResult bakingResult) {
        Map<Identifier, BakedModel> extraBakedModels = new Object2ObjectLinkedOpenHashMap<>();
        for (Identifier extraModelPath : extraModels) {
            ModelDebugName debugName = () -> extraModelPath.toString() + "#extra";
            BakedModel baked =
                bakery.new ModelBakerImpl(textureGetter).bake(extraModelPath, BlockModelRotation.X0_Y0);
            extraBakedModels.put(extraModelPath, baked);
        }
        ((Duck_ModelBakeryBakingResult) (Object) bakingResult).krender$setExtraModels(extraBakedModels);
    }
}
