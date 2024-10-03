package com.kneelawk.krender.reloadlistener.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;

import com.kneelawk.krender.reloadlistener.api.ReloadContext;

public record ReloadContextImpl(PackType packType, ResourceManager resourceManager,
                                List<PreparableReloadListener> listeners, Executor backgroundExecutor,
                                Executor gameExecutor, CompletableFuture<Unit> alsoWaitedFor, boolean profiled)
    implements ReloadContext {
    @Override
    public PackType getPackType() {
        return packType;
    }

    @Override
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    @Override
    public List<PreparableReloadListener> getListeners() {
        return listeners;
    }

    @Override
    public Executor getBackgroundExecutor() {
        return backgroundExecutor;
    }

    @Override
    public Executor getGameExecutor() {
        return gameExecutor;
    }

    @Override
    public CompletableFuture<Unit> getAlsoWaitedFor() {
        return alsoWaitedFor;
    }

    @Override
    public boolean isProfiled() {
        return profiled;
    }
}
