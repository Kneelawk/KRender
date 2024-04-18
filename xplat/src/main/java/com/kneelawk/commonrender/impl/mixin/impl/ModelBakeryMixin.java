package com.kneelawk.commonrender.impl.mixin.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;

import com.kneelawk.commonrender.impl.mixin.api.ModelBakeryHooks;
import com.kneelawk.commonrender.impl.model.LoadedModelDispatcher;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin implements ModelBakeryHooks {
    @Unique
    private LoadedModelDispatcher common_render$dispatcher;

    @Inject(method = "<init>",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",
            ordinal = 0))
    private void common_render$init(BlockColors blockColors, ProfilerFiller profilerFiller,
                                    Map<ResourceLocation, BlockModel> map,
                                    Map<ResourceLocation, List<ModelBakery.LoadedJson>> map2, CallbackInfo ci) {
        CompletableFuture<LoadedModelDispatcher> future = LoadedModelDispatcher.DISPATCHER_FUTURE.getAndSet(null);
        if (future != null) {
            // should already be complete by now
            common_render$dispatcher = future.join();
        } else {
            common_render$dispatcher = null;
        }
        System.out.println("dispatcher: " + common_render$dispatcher);
    }

    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void common_render$loadModel(ResourceLocation resourceLocation, CallbackInfo ci) {

    }
}
