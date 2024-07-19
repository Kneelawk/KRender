package com.kneelawk.krender.impl.mixin.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;

import com.kneelawk.krender.impl.KRLog;
import com.kneelawk.krender.impl.loading.ModelBakeryPluginContext;
import com.kneelawk.krender.impl.loading.ModelBakeryPluginManager;
import com.kneelawk.krender.impl.loading.ModelBakeryPluginRegistrar;
import com.kneelawk.krender.impl.loading.PreparedModelBakeryPluginList;
import com.kneelawk.krender.impl.mixin.api.ModelBakeryHooks;

// This mixin is heavily based on the Fabric API ModelLoaderMixin class
// https://github.com/FabricMC/fabric/blob/166f144fc9c322d29edebf5a25c0dda9444295da/fabric-model-loading-api-v1/src/client/java/net/fabricmc/fabric/mixin/client/model/loading/ModelLoaderMixin.java

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin implements ModelBakeryHooks {
    @Shadow
    @Final
    public static ModelResourceLocation MISSING_MODEL_LOCATION;

    @Shadow
    @Final
    private Map<ResourceLocation, UnbakedModel> unbakedCache;

    @Shadow
    @Final
    private Map<ResourceLocation, UnbakedModel> topLevelModels;

    @Shadow
    protected abstract void loadTopLevel(ModelResourceLocation arg);

    @Shadow
    public abstract UnbakedModel getModel(ResourceLocation arg);

    @Shadow
    @Final
    private Set<ResourceLocation> loadingStack;

    @Shadow
    protected abstract void loadModel(ResourceLocation arg) throws Exception;

    @Unique
    private int krender$recursionGuard = 0;

    @Unique
    private ModelBakeryPluginManager krender$manager;

    @Inject(method = "<init>",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",
            ordinal = 0))
    private void krender$init(BlockColors blockColors, ProfilerFiller profilerFiller,
                              Map<ResourceLocation, BlockModel> map,
                              Map<ResourceLocation, List<ModelBakery.LoadedJson>> map2, CallbackInfo ci) {
        if (!unbakedCache.containsKey(MISSING_MODEL_LOCATION)) {
            throw new AssertionError(
                "ModelBakery in unexpected state. Something is likely tampering with model loading.");
        }

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

    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void krender$loadModel(ResourceLocation resourceLocation, CallbackInfo ci) {
        if (krender$manager == null) return;

        krender$recursionGuard++;
        try {
            if (krender$manager.loadModel(resourceLocation)) {
                ci.cancel();
            }
        } finally {
            krender$recursionGuard--;
        }
    }

    @Override
    public void krender$putModel(ResourceLocation name, UnbakedModel model) {
        unbakedCache.put(name, model);
        loadingStack.addAll(model.getDependencies());
    }

    @Override
    public UnbakedModel krender$getOrLoadModel(ResourceLocation name) {
        if (unbakedCache.containsKey(name)) {
            return unbakedCache.get(name);
        }

        if (!loadingStack.add(name)) {
            throw new IllegalStateException("Circular reference while loading " + name);
        }

        try {
            loadModel(name);
        } catch (Exception e) {
            KRLog.LOGGER.error("Unable to load model: '{}'", name, e);
            unbakedCache.put(name, unbakedCache.get(MISSING_MODEL_LOCATION));
        } finally {
            loadingStack.remove(name);
        }

        return unbakedCache.get(name);
    }

    @Unique
    private void krender$addExtraName(ResourceLocation name) {
        if (name instanceof ModelResourceLocation modelName) {
            loadTopLevel(modelName);
        } else {
            UnbakedModel model = getModel(name);
            unbakedCache.put(name, model);
            topLevelModels.put(name, model);
        }
    }

    @Unique
    private void krender$addExtraModel(ResourceLocation name, UnbakedModel model) {
        unbakedCache.put(name, model);
        topLevelModels.put(name, model);
    }
}
