package com.kneelawk.krender.reloadlistener.impl.mixin.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.Unit;

import com.kneelawk.krender.reloadlistener.api.ReloadListenerEvents;
import com.kneelawk.krender.reloadlistener.impl.ReloadContextImpl;
import com.kneelawk.krender.reloadlistener.impl.ReloadListenerStatusImpl;
import com.kneelawk.krender.reloadlistener.impl.mixin.api.Duck_MultiPackResourceManager;

@Mixin(SimpleReloadInstance.class)
public class Mixin_SimpleReloadInstance {
    @Inject(method = "create", at = @At("HEAD"))
    private static void onCreate(ResourceManager resourceManager, List<PreparableReloadListener> listeners,
                                 Executor backgroundExecutor, Executor gameExecutor,
                                 CompletableFuture<Unit> alsoWaitedFor, boolean profiled,
                                 CallbackInfoReturnable<ReloadInstance> cir) {
        if (resourceManager instanceof Duck_MultiPackResourceManager duck) {
            PackType type = duck.krender_reload_listener$getPackType();
            if (type == PackType.CLIENT_RESOURCES) {
                ReloadListenerStatusImpl.resourcePackReloading.set(true);
            } else {
                ReloadListenerStatusImpl.dataPackReloading.set(true);
            }

            ReloadListenerEvents.PRE_RELOAD.invoker().beforeReload(
                new ReloadContextImpl(type, resourceManager, listeners, backgroundExecutor, gameExecutor, alsoWaitedFor,
                    profiled));
        }
    }

    @Inject(method = "create", at = @At("RETURN"))
    private static void onCreateReturn(ResourceManager resourceManager, List<PreparableReloadListener> listeners,
                                       Executor backgroundExecutor, Executor gameExecutor,
                                       CompletableFuture<Unit> alsoWaitedFor, boolean profiled,
                                       CallbackInfoReturnable<ReloadInstance> cir) {
        if (resourceManager instanceof Duck_MultiPackResourceManager duck) {
            final PackType type = duck.krender_reload_listener$getPackType();
            cir.getReturnValue().done().thenRunAsync(() -> {
                if (type == PackType.CLIENT_RESOURCES) {
                    ReloadListenerStatusImpl.resourcePackReloading.set(false);
                } else {
                    ReloadListenerStatusImpl.dataPackReloading.set(false);
                }

                ReloadListenerEvents.POST_RELOAD.invoker().afterReload(
                    new ReloadContextImpl(type, resourceManager, listeners, backgroundExecutor, gameExecutor,
                        alsoWaitedFor, profiled));
            }, gameExecutor);
        }
    }
}
