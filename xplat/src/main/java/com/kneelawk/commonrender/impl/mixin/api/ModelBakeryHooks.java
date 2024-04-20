package com.kneelawk.commonrender.impl.mixin.api;

import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public interface ModelBakeryHooks {
    void common_render$putModel(ResourceLocation name, UnbakedModel model);

    UnbakedModel common_render$getOrLoadModel(ResourceLocation name);
}
