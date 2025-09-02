package com.kneelawk.krender.model.loading.impl.mixin.impl;

import java.util.Map;

import net.minecraft.client.renderer.block.model.BlockStateModel;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.model.loading.impl.mixin.api.Duck_ModelBakeryBakingResult;

@Mixin(ModelBakery.BakingResult.class)
public class Mixin_ModelBakeryBakingResult implements Duck_ModelBakeryBakingResult {
    @Unique
    private @Nullable Map<ResourceLocation, BlockStateModel> krender$extraModels;

    @Override
    public void krender$setExtraModels(Map<ResourceLocation, BlockStateModel> extraModels) {
        krender$extraModels = extraModels;
    }

    @Override
    public Map<ResourceLocation, BlockStateModel> krender$getExtraModels() {
        return krender$extraModels;
    }
}
