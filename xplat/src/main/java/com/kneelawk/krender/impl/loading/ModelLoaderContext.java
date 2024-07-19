package com.kneelawk.krender.impl.loading;

import java.util.Objects;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.api.loading.ModelLoader;
import com.kneelawk.krender.impl.mixin.api.ModelBakeryHooks;

public class ModelLoaderContext implements ModelLoader.Context {
    private final ResourceLocation location;
    private final ModelBakery bakery;
    private final ModelBakeryHooks hooks;
    boolean loaded = false;

    public ModelLoaderContext(ResourceLocation location, ModelBakery bakery, ModelBakeryHooks hooks) {
        this.location = location;
        this.bakery = bakery;
        this.hooks = hooks;
    }

    @Override
    public void putModel(ResourceLocation name, UnbakedModel model) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(model, "model must not be null");

        if (name.equals(location)) {
            loaded = true;
        }

        hooks.krender$putModel(name, model);
    }

    @Override
    public UnbakedModel getOrLoadModel(ResourceLocation name) {
        Objects.requireNonNull(name, "name must not be null");

        return hooks.krender$getOrLoadModel(name);
    }

    @Override
    public ResourceLocation location() {return location;}

    @Override
    public ModelBakery bakery() {return bakery;}
}
