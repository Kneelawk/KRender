package com.kneelawk.krender.impl.loading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.api.loading.ModelBakeryPlugin;
import com.kneelawk.krender.api.loading.ModelProvider;

public class ModelBakeryPluginContext implements ModelBakeryPlugin.Context {
    private final Set<ModelResourceLocation> extraNames = new LinkedHashSet<>();
    private final Map<ModelResourceLocation, UnbakedModel> extraModels = new LinkedHashMap<>();
    private final List<ModelProvider> loaders = new ArrayList<>();

    @Override
    public void addModels(ModelResourceLocation... names) {
        Collections.addAll(extraNames, names);
    }

    @Override
    public void addModels(Collection<? extends ModelResourceLocation> names) {
        extraNames.addAll(names);
    }

    @Override
    public void addModel(ModelResourceLocation name, UnbakedModel model) {
        extraModels.put(name, model);
    }

    @Override
    public void addModels(Map<? extends ModelResourceLocation, ? extends UnbakedModel> models) {
        extraModels.putAll(models);
    }

    @Override
    public void registerModelLoader(ModelProvider loader) {
        Objects.requireNonNull(loader, "loader cannot be null");
        
        loaders.add(loader);
    }
    
    public void addExtraNames(Consumer<ModelResourceLocation> adder) {
        extraNames.forEach(adder);
    }
    
    public void addExtraModels(BiConsumer<ModelResourceLocation, UnbakedModel> adder) {
        extraModels.forEach(adder);
    }
    
    public ModelBakeryPluginManager createManager(ModelBakery bakery) {
        return new ModelBakeryPluginManager(bakery, loaders.toArray(ModelProvider[]::new));
    }
}
