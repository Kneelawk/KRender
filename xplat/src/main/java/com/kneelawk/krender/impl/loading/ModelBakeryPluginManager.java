package com.kneelawk.krender.impl.loading;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.api.loading.ModelLoader;
import com.kneelawk.krender.impl.KRLog;
import com.kneelawk.krender.impl.mixin.api.ModelBakeryHooks;

public class ModelBakeryPluginManager {
    private final ModelBakery modelBakery;
    private final ModelLoader[] loaders;

    public ModelBakeryPluginManager(ModelBakery modelBakery, ModelLoader[] loaders) {
        this.modelBakery = modelBakery;
        this.loaders = loaders;
    }

    public boolean loadModel(ResourceLocation resourceLocation) {
        ModelLoaderContext ctx = new ModelLoaderContext(resourceLocation, modelBakery, (ModelBakeryHooks) modelBakery);

        for (ModelLoader loader : loaders) {
            try {
                loader.loadModels(ctx);
                if (ctx.loaded) return true;
            } catch (Exception e) {
                KRLog.LOGGER.error("Error loading model from custom model loader", e);
            }
        }

        return false;
    }
}
