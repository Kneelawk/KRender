package com.kneelawk.krender.model.loading.impl.loading;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.model.loading.api.BlockStateModelProvider;
import com.kneelawk.krender.model.loading.api.LowLevelModelProvider;
import com.kneelawk.krender.model.loading.api.ModelBakeryPlugin;
import com.kneelawk.krender.model.loading.impl.mixin.api.ModelBakeryHooks;

public class ModelBakeryPluginContextImpl implements ModelBakeryPlugin.Context {
    private final Map<ModelResourceLocation, ResourceLocation> extraTopLevelNames = new LinkedHashMap<>();
    private final Map<ModelResourceLocation, UnbakedModel> extraTopLevelModels = new LinkedHashMap<>();
    private final Map<ResourceLocation, UnbakedModel> extraLowLevelModels = new LinkedHashMap<>();
    private final List<LowLevelModelProvider> lowLevelProviders = new ArrayList<>();
    private final Map<ResourceLocation, BlockStateModelProviderBuilder> blockStateProviders = new LinkedHashMap<>();

    @Override
    public void addTopLevelModelName(ModelResourceLocation name, ResourceLocation path) {
        extraTopLevelNames.put(name, path);
        blockStateProviders.computeIfAbsent(name.id(), id -> BlockStateModelProviderBuilder.empty())
            .extraTopLevelNames().put(name, path);
    }

    @Override
    public void addTopLevelModelNames(Map<ModelResourceLocation, ResourceLocation> models) {
        extraTopLevelNames.putAll(models);
        for (Map.Entry<ModelResourceLocation, ResourceLocation> model : models.entrySet()) {
            blockStateProviders.computeIfAbsent(model.getKey().id(), id -> BlockStateModelProviderBuilder.empty())
                .extraTopLevelNames().put(model.getKey(), model.getValue());
        }
    }

    @Override
    public void addTopLevelModel(ModelResourceLocation name, UnbakedModel model) {
        extraTopLevelModels.put(name, model);
        blockStateProviders.computeIfAbsent(name.id(), id -> BlockStateModelProviderBuilder.empty())
            .extraTopLevelModels().put(name, model);
    }

    @Override
    public void addTopLevelModels(Map<ModelResourceLocation, ? extends UnbakedModel> models) {
        extraTopLevelModels.putAll(models);
        for (Map.Entry<ModelResourceLocation, ? extends UnbakedModel> model : models.entrySet()) {
            blockStateProviders.computeIfAbsent(model.getKey().id(), id -> BlockStateModelProviderBuilder.empty())
                .extraTopLevelModels().put(model.getKey(), model.getValue());
        }
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
    public void registerBlockStateModelProvider(ResourceLocation blockId, BlockStateModelProvider provider) {
        Objects.requireNonNull(provider, "provider cannot be null");

        blockStateProviders.computeIfAbsent(blockId, id -> BlockStateModelProviderBuilder.empty()).blockStateProviders()
            .add(provider);
    }

    public ModelBakeryPluginManager createManager(ModelBakery bakery) {
        var providers = blockStateProviders.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue().build()))
            .collect(ImmutableMap.toImmutableMap(Entry::key, Entry::value));

        return new ModelBakeryPluginManager(bakery, (ModelBakeryHooks) bakery, extraTopLevelNames, extraTopLevelModels,
            extraLowLevelModels, lowLevelProviders.toArray(LowLevelModelProvider[]::new), providers
        );
    }

    private record BlockStateModelProviderBuilder(
        ImmutableMap.Builder<ModelResourceLocation, ResourceLocation> extraTopLevelNames,
        ImmutableMap.Builder<ModelResourceLocation, UnbakedModel> extraTopLevelModels,
        List<BlockStateModelProvider> blockStateProviders) {
        public static BlockStateModelProviderBuilder empty() {
            return new BlockStateModelProviderBuilder(ImmutableMap.builder(), ImmutableMap.builder(),
                new ArrayList<>());
        }

        public BlockStateModelProviderHolder build() {
            return new BlockStateModelProviderHolder(extraTopLevelNames.build(), extraTopLevelModels.build(),
                blockStateProviders.toArray(BlockStateModelProvider[]::new));
        }
    }

    private record Entry(ResourceLocation key, BlockStateModelProviderHolder value) {}
}
