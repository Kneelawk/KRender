package com.kneelawk.krender.model.loading.impl.mixin.impl;

import net.minecraft.client.resources.model.SpriteGetter;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resources.model.ModelBakery;

import com.kneelawk.krender.model.loading.impl.loading.ModelManagerPluginManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ModelBakery.class)
public class Mixin_ModelBakery {
    @Unique
    private ModelManagerPluginManager krender$manager;

    @Inject(method = "name=/<init>/", at = @At("RETURN"))
    private void krender$init(CallbackInfo ci) {
        krender$manager = ModelManagerPluginManager.CURRENT_MANAGER.get();
    }

    @Inject(method = "bakeModels", at = @At("RETURN"))
    private void krender$bakeExtraModels(SpriteGetter spriteGetter, Executor executor,
                                         CallbackInfoReturnable<CompletableFuture<ModelBakery.BakingResult>> cir) {
        krender$manager.bakeExtraModels((ModelBakery) (Object) this, spriteGetter, cir.getReturnValue());
    }
}
