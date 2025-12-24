package com.kneelawk.krender.model.loading.impl.mixin.api;

import java.util.Map;

import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.resources.Identifier;

public interface Duck_ModelBakeryBakingResult {
    void krender$setExtraModels(Map<Identifier, QuadCollection> extraModels);

    Map<Identifier, QuadCollection> krender$getExtraModels();
}
