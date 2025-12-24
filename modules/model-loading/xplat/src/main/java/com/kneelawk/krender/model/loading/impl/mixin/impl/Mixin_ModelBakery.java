package com.kneelawk.krender.model.loading.impl.mixin.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.SpriteGetter;

import com.kneelawk.krender.model.loading.impl.loading.ModelManagerPluginManager;

@Mixin(ModelBakery.class)
public class Mixin_ModelBakery {
    @Unique
    private ModelManagerPluginManager krender$manager;

    @Inject(method = "name=/<init>/", at = @At("RETURN"))
    private void krender$init(CallbackInfo ci) {
        krender$manager = ModelManagerPluginManager.CURRENT_MANAGER.get();
    }

    @Inject(method = "bakeModels", at = @At("RETURN"), cancellable = true)
    private void krender$bakeExtraModels(SpriteGetter spriteGetter, Executor executor,
                                         CallbackInfoReturnable<CompletableFuture<ModelBakery.BakingResult>> cir, @Local
                                         ModelBakery.ModelBakerImpl modelBaker) {
        cir.setReturnValue(
            krender$manager.bakeExtraModels((ModelBakery) (Object) this, modelBaker, executor, cir.getReturnValue()));
    }
}
