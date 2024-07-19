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
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.api.loading.ModelBakeryPlugin;
import com.kneelawk.krender.api.loading.ModelLoader;

public class ModelBakeryPluginContext implements ModelBakeryPlugin.Context {
    private final Set<ResourceLocation> extraNames = new LinkedHashSet<>();
    private final Map<ResourceLocation, UnbakedModel> extraModels = new LinkedHashMap<>();
    private final List<ModelLoader> loaders = new ArrayList<>();

    @Override
    public void addModels(ResourceLocation... names) {
        Collections.addAll(extraNames, names);
    }

    @Override
    public void addModels(Collection<? extends ResourceLocation> names) {
        extraNames.addAll(names);
    }

    @Override
    public void addModel(ResourceLocation name, UnbakedModel model) {
        extraModels.put(name, model);
    }

    @Override
    public void addModels(Map<? extends ResourceLocation, ? extends UnbakedModel> models) {
        extraModels.putAll(models);
    }

    @Override
    public void registerModelLoader(ModelLoader loader) {
        Objects.requireNonNull(loader, "loader cannot be null");
        
        loaders.add(loader);
    }
    
    public void addExtraNames(Consumer<ResourceLocation> adder) {
        extraNames.forEach(adder);
    }
    
    public void addExtraModels(BiConsumer<ResourceLocation, UnbakedModel> adder) {
        extraModels.forEach(adder);
    }
    
    public ModelBakeryPluginManager createManager(ModelBakery bakery) {
        return new ModelBakeryPluginManager(bakery, loaders.toArray(ModelLoader[]::new));
    }
}
