package com.kneelawk.commonrender.impl.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.commonrender.impl.mixin.api.ModelBakeryHooks;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin implements ModelBakeryHooks {
    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void common_render$loadModel(ResourceLocation resourceLocation, CallbackInfo ci) {
        
    }
}
