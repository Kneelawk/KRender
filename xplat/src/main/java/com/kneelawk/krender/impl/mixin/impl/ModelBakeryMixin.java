package com.kneelawk.krender.impl.mixin.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;

import com.kneelawk.krender.impl.loading.ModelBakeryPluginContext;
import com.kneelawk.krender.impl.loading.ModelBakeryPluginManager;
import com.kneelawk.krender.impl.loading.ModelBakeryPluginRegistrar;
import com.kneelawk.krender.impl.loading.PreparedModelBakeryPluginList;

// This mixin is heavily based on the Fabric API ModelLoaderMixin class
// https://github.com/FabricMC/fabric/blob/166f144fc9c322d29edebf5a25c0dda9444295da/fabric-model-loading-api-v1/src/client/java/net/fabricmc/fabric/mixin/client/model/loading/ModelLoaderMixin.java

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
    @Shadow
    @Final
    public static ModelResourceLocation MISSING_MODEL_VARIANT;
    @Shadow
    @Final
    private Map<ModelResourceLocation, UnbakedModel> topLevelModels;
    @Shadow
    @Final
    private Map<ResourceLocation, UnbakedModel> unbakedCache;
    @Shadow
    @Final
    private Set<ResourceLocation> loadingStack;

    @Shadow
    abstract UnbakedModel getModel(ResourceLocation resourceLocation);

    @Shadow
    protected abstract void registerModelAndLoadDependencies(ModelResourceLocation modelResourceLocation,
                                                             UnbakedModel unbakedModel);

    @Unique
    private int krender$recursionGuard = 0;

    @Unique
    private ModelBakeryPluginManager krender$manager;

    @Inject(method = "<init>",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/resources/model/BlockStateModelLoader;<init>(Ljava/util/Map;Lnet/minecraft/util/profiling/ProfilerFiller;Lnet/minecraft/client/resources/model/UnbakedModel;Lnet/minecraft/client/color/block/BlockColors;Ljava/util/function/BiConsumer;)V"))
    private void krender$init(BlockColors blockColors, ProfilerFiller profilerFiller,
                              Map<ResourceLocation, BlockModel> map,
                              Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> map2, CallbackInfo ci) {
        if (!topLevelModels.containsKey(MISSING_MODEL_VARIANT)) {
            throw new AssertionError(
                "ModelBakery in unexpected state. Something is likely tampering with model loading.");
        }

        profilerFiller.popPush("krender");

        CompletableFuture<PreparedModelBakeryPluginList> future =
            ModelBakeryPluginRegistrar.PREPARE_FUTURE.getAndSet(null);
        if (future != null) {
            // should already be complete by now
            ModelBakeryPluginContext ctx = future.join().loadPlugins();
            ctx.addExtraNames(this::krender$addExtraName);
            ctx.addExtraModels(this::krender$addExtraModel);
            krender$manager = ctx.createManager((ModelBakery) (Object) this);
        } else {
            krender$manager = null;
        }
    }

    @Inject(method = "getModel", at = @At("HEAD"))
    private void krender$getModel(ResourceLocation resourceLocation, CallbackInfoReturnable<UnbakedModel> cir) {
        if (krender$recursionGuard > 0) {
            throw new IllegalStateException("ModelBakery.getModel called during model loading.");
        }
    }

    @ModifyExpressionValue(method = "getModel",
        at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z", remap = false, ordinal = 1))
    private boolean krender$wrapLoadBlockModel(boolean skipLoading, @Local(ordinal = 1) ResourceLocation toLoad) {
        return skipLoading || krender$loadModel(toLoad);
    }

    @Unique
    private boolean krender$loadModel(ResourceLocation name) {
        if (krender$manager == null) return false;

        krender$recursionGuard++;
        try {
            UnbakedModel model = krender$manager.loadModel(name);
            if (model != null) {
                unbakedCache.put(name, model);
                loadingStack.addAll(model.getDependencies());
                return true;
            }
        } finally {
            krender$recursionGuard--;
        }

        return false;
    }

    @Unique
    private void krender$addExtraName(ModelResourceLocation name) {
        UnbakedModel model = getModel(name.id());
        registerModelAndLoadDependencies(name, model);
    }

    @Unique
    private void krender$addExtraModel(ModelResourceLocation name, UnbakedModel model) {
        registerModelAndLoadDependencies(name, model);
    }
}
