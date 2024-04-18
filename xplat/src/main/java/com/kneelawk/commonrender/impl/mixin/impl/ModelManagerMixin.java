package com.kneelawk.commonrender.impl.mixin.impl;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.commonrender.impl.model.LoadedModelDispatcher;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @ModifyExpressionValue(method = "reload", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/resources/model/ModelManager;loadBlockModels(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<Map<ResourceLocation, BlockModel>> addDependency(
        CompletableFuture<Map<ResourceLocation, BlockModel>> blockModels) {
        LoadedModelDispatcher.collectModelLoaders();
        return blockModels.thenCombine(LoadedModelDispatcher.DISPATCHER_FUTURE.get(), (a, b) -> a);
    }
}
