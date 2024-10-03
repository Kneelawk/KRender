package com.kneelawk.krender.reloadlistener.impl.mixin.impl;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;

import com.kneelawk.krender.reloadlistener.impl.mixin.api.Duck_MultiPackResourceManager;

@Mixin(MultiPackResourceManager.class)
public class Mixin_MultiPackResourceManager implements Duck_MultiPackResourceManager {
    @Unique
    private PackType krender_reload_listener$packType;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(PackType type, List<PackResources> packs, CallbackInfo ci) {
        krender_reload_listener$packType = type;
    }

    @Override
    public PackType krender_reload_listener$getPackType() {
        return krender_reload_listener$packType;
    }
}
