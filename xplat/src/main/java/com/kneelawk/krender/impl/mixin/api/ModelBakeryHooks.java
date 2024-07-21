package com.kneelawk.krender.impl.mixin.api;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public interface ModelBakeryHooks {
    UnbakedModel krender$getOrLoadModel(ResourceLocation name);
    
    boolean krender$topLevelModelExists(ModelResourceLocation name);
}
