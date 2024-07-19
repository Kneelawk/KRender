package com.kneelawk.krender.impl.mixin.api;

import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public interface ModelBakeryHooks {
    void krender$putModel(ResourceLocation name, UnbakedModel model);

    UnbakedModel krender$getOrLoadModel(ResourceLocation name);
}
