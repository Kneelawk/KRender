package com.kneelawk.krender.model.loading.impl.mixin.impl;

import java.util.Map;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;

import com.kneelawk.krender.model.loading.impl.mixin.api.Duck_ModelBakeryBakingResult;

@Mixin(ModelBakery.BakingResult.class)
public class Mixin_ModelBakeryBakingResult implements Duck_ModelBakeryBakingResult {
    @Unique
    private @Nullable Map<Identifier, BakedModel> krender$extraModels;

    @Override
    public void krender$setExtraModels(Map<Identifier, BakedModel> extraModels) {
        krender$extraModels = extraModels;
    }

    @Override
    public Map<Identifier, BakedModel> krender$getExtraModels() {
        return krender$extraModels;
    }
}
