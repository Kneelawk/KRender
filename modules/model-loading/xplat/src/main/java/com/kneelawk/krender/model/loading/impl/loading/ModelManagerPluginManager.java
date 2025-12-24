package com.kneelawk.krender.model.loading.impl.loading;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

import com.kneelawk.krender.model.loading.impl.mixin.api.Duck_ModelBakeryBakingResult;

public class ModelManagerPluginManager {
    public static final ThreadLocal<ModelManagerPluginManager> CURRENT_MANAGER = new ThreadLocal<>();

    private final Map<Identifier, UnbakedModel> referenceableModels;
    private final Map<BlockState, BlockStateModel.UnbakedRoot> blockStateModels;
    private final Set<Identifier> extraModels;

    public ModelManagerPluginManager(Map<Identifier, UnbakedModel> referenceableModels,
                                     Map<BlockState, BlockStateModel.UnbakedRoot> blockStateModels,
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
        if (!(models.models() instanceof HashMap<BlockState, BlockStateModel.UnbakedRoot>)) {
            models = new BlockStateModelLoader.LoadedModels(new LinkedHashMap<>(models.models()));
        }

        models.models().putAll(blockStateModels);

        return models;
    }

    public void resolveExtraModels(ResolvableModel.Resolver resolver) {
        for (Identifier extraModelPath : extraModels) {
            resolver.markDependency(extraModelPath);
        }
    }

    public CompletableFuture<ModelBakery.BakingResult> bakeExtraModels(ModelBakery bakery, ModelBaker modelBaker,
                                                                       Executor executor,
                                                                       CompletableFuture<ModelBakery.BakingResult> bakingResultFuture) {
        CompletableFuture<Map<Identifier, QuadCollection>> extraBakedModelsFuture =
            CompletableFuture.supplyAsync(() -> {
                Map<Identifier, QuadCollection> extraBakedModels = new Object2ObjectLinkedOpenHashMap<>();
                for (Identifier extraModelPath : extraModels) {
                    ResolvedModel model = modelBaker.getModel(extraModelPath);
                    QuadCollection baked =
                        model.bakeTopGeometry(model.getTopTextureSlots(), modelBaker, BlockModelRotation.IDENTITY);
                    extraBakedModels.put(extraModelPath, baked);
                }
                return extraBakedModels;
            }, executor);

        return bakingResultFuture.thenCombine(extraBakedModelsFuture, (bakingResult, extraBakedModels) -> {
            ((Duck_ModelBakeryBakingResult) (Object) bakingResult).krender$setExtraModels(extraBakedModels);
            return bakingResult;
        });
    }
}
