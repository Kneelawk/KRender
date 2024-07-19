package com.kneelawk.krender.impl.mixin.impl;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import com.kneelawk.krender.impl.loading.ModelBakeryPluginRegistrar;

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @ModifyExpressionValue(method = "reload", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/resources/model/ModelManager;loadBlockModels(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<Map<ResourceLocation, BlockModel>> addDependency(
        CompletableFuture<Map<ResourceLocation, BlockModel>> blockModels,
        PreparableReloadListener.PreparationBarrier barrier, ResourceManager resourceManager,
        ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler, Executor prepareExecutor,
        Executor applyExecutor) {
        ModelBakeryPluginRegistrar.preparePlugins(resourceManager, prepareExecutor);
        return blockModels.thenCombine(ModelBakeryPluginRegistrar.PREPARE_FUTURE.get(), (a, b) -> a);
    }
}
