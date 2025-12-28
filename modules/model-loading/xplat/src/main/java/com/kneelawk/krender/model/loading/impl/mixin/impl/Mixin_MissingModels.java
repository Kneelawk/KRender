package com.kneelawk.krender.model.loading.impl.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.SpriteGetter;

import com.kneelawk.krender.model.loading.impl.mixin.api.Duck_MissingModels;

@Mixin(ModelBakery.MissingModels.class)
public class Mixin_MissingModels implements Duck_MissingModels {
    @Unique
    private QuadCollection krender$missingModelQuads;

    @Inject(method = "bake", at = @At("RETURN"))
    private static void krender$onBake(ResolvedModel $$0, SpriteGetter $$1, ModelBaker.PartCache $$2,
                                       CallbackInfoReturnable<ModelBakery.MissingModels> cir,
                                       @Local QuadCollection collection) {
        ((Duck_MissingModels) (Object) cir.getReturnValue()).krender$setMissingModelQuads(collection);
    }

    @Override
    public void krender$setMissingModelQuads(QuadCollection collection) {
        krender$missingModelQuads = collection;
    }

    @Override
    public QuadCollection krender$getMissingModelQuads() {
        return krender$missingModelQuads;
    }
}
