package com.kneelawk.krender.impl.loading;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.api.loading.ModelProvider;
import com.kneelawk.krender.impl.KRLog;

public class ModelBakeryPluginManager {
    private final ModelBakery modelBakery;
    private final ModelProvider[] loaders;

    public ModelBakeryPluginManager(ModelBakery modelBakery, ModelProvider[] loaders) {
        this.modelBakery = modelBakery;
        this.loaders = loaders;
    }

    public UnbakedModel loadModel(ResourceLocation resourceLocation) {
        ModelLoaderContext ctx = new ModelLoaderContext(resourceLocation, modelBakery);

        for (ModelProvider loader : loaders) {
            try {
                UnbakedModel model = loader.loadModel(ctx);
                if (model != null) return model;
            } catch (Exception e) {
                KRLog.LOGGER.error("Error loading model from custom model loader", e);
            }
        }

        return null;
    }
}
