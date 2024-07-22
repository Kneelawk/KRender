package com.kneelawk.krender.model.loading.impl.mixin.impl;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import com.kneelawk.krender.model.loading.impl.loading.ModelBakeryPluginManager;

@Mixin(BlockStateModelLoader.class)
public class BlockStateModelLoaderMixin {
    @Shadow
    @Final
    private BiConsumer<ModelResourceLocation, UnbakedModel> discoveredModelOutput;

    @Shadow
    @Final
    private BlockColors blockColors;
    @Unique
    private ModelBakeryPluginManager krender$manager;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void krender$init(Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> map,
                              ProfilerFiller profilerFiller, UnbakedModel unbakedModel, BlockColors blockColors,
                              BiConsumer<ModelResourceLocation, UnbakedModel> biConsumer, CallbackInfo ci) {
        krender$manager = ModelBakeryPluginManager.CURRENT_MANAGER.get();
    }

    @Inject(method = "loadBlockStateDefinitions", at = @At("HEAD"), cancellable = true)
    private void krender$onLoadBlockStateDefinitions(ResourceLocation resourceLocation,
                                                     StateDefinition<Block, BlockState> stateDefinition,
                                                     CallbackInfo ci) {
        if (krender$manager != null) {
            if (krender$manager.loadBlockStateModels(resourceLocation, stateDefinition, blockColors, discoveredModelOutput)) {
                ci.cancel();
            }
        }
    }
}
