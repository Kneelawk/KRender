package com.kneelawk.commonrender.impl.loading;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.commonrender.api.loading.ModelLoader;
import com.kneelawk.commonrender.impl.CRLog;
import com.kneelawk.commonrender.impl.mixin.api.ModelBakeryHooks;

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
                CRLog.LOGGER.error("Error loading model from custom model loader", e);
            }
        }

        return false;
    }
}
