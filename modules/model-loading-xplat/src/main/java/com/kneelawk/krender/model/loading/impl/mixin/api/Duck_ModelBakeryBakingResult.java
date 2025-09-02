package com.kneelawk.krender.model.loading.impl.mixin.api;

import java.util.Map;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;

public interface Duck_ModelBakeryBakingResult {
    void krender$setExtraModels(Map<ResourceLocation, BlockStateModel> extraModels);

    Map<ResourceLocation, BlockStateModel> krender$getExtraModels();
}
