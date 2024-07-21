package com.kneelawk.krender.impl.loading;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.api.loading.BlockStateModelProvider;
import com.kneelawk.krender.api.loading.LowLevelModelProvider;
import com.kneelawk.krender.api.loading.ModelBakeryPlugin;
import com.kneelawk.krender.impl.mixin.api.ModelBakeryHooks;

public class ModelBakeryPluginContextImpl implements ModelBakeryPlugin.Context {
    private final Map<ModelResourceLocation, ResourceLocation> extraTopLevelNames = new LinkedHashMap<>();
    private final Map<ModelResourceLocation, UnbakedModel> extraTopLevelModels = new LinkedHashMap<>();
    private final Map<ResourceLocation, UnbakedModel> extraLowLevelModels = new LinkedHashMap<>();
    private final List<LowLevelModelProvider> lowLevelProviders = new ArrayList<>();
    private final List<BlockStateModelProvider> blockStateProviders = new ArrayList<>();

    @Override
    public void addTopLevelModelName(ModelResourceLocation name, ResourceLocation path) {
        extraTopLevelNames.put(name, path);
    }

    @Override
    public void addTopLevelModelNames(Map<ModelResourceLocation, ResourceLocation> models) {
        extraTopLevelNames.putAll(models);
    }

    @Override
    public void addTopLevelModel(ModelResourceLocation name, UnbakedModel model) {
        extraTopLevelModels.put(name, model);
    }

    @Override
    public void addTopLevelModels(Map<ModelResourceLocation, ? extends UnbakedModel> models) {
        extraTopLevelModels.putAll(models);
    }

    @Override
    public void addLowLevelModel(ResourceLocation path, UnbakedModel model) {
        extraLowLevelModels.put(path, model);
    }

    @Override
    public void addLowLevelModels(Map<ResourceLocation, ? extends UnbakedModel> models) {
        extraLowLevelModels.putAll(models);
    }

    @Override
    public void registerLowLevelModelProvider(LowLevelModelProvider provider) {
        Objects.requireNonNull(provider, "provider cannot be null");

        lowLevelProviders.add(provider);
    }

    @Override
    public void registerBlockStateModelProvider(BlockStateModelProvider provider) {
        Objects.requireNonNull(provider, "provider cannot be null");

        blockStateProviders.add(provider);
    }

    public ModelBakeryPluginManager createManager(ModelBakery bakery) {
        return new ModelBakeryPluginManager(bakery, (ModelBakeryHooks) bakery, extraTopLevelNames, extraTopLevelModels,
            extraLowLevelModels, lowLevelProviders.toArray(LowLevelModelProvider[]::new),
            blockStateProviders.toArray(BlockStateModelProvider[]::new));
    }
}
