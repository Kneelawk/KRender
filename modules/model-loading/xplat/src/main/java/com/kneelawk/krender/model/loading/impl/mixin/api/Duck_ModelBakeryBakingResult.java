package com.kneelawk.krender.model.loading.impl.mixin.api;

import java.util.Map;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.Identifier;

public interface Duck_ModelBakeryBakingResult {
    void krender$setExtraModels(Map<Identifier, BakedModel> extraModels);

    Map<Identifier, BakedModel> krender$getExtraModels();
}
