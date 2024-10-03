package com.kneelawk.krender.reloadlistener.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;

/**
 * Describes the context of a resource reload.
 */
public interface ReloadContext {
    /**
     * {@return the pack type of this resource reload}
     */
    PackType getPackType();

    /**
     * {@return the resource manager used for this reload}
     */
    ResourceManager getResourceManager();

    /**
     * {@return the reload listeners being reloaded}
     */
    List<PreparableReloadListener> getListeners();

    /**
     * {@return the executor that the prepare stage happens on}
     */
    Executor getBackgroundExecutor();

    /**
     * {@return the executor that the apply stage happens on}
     */
    Executor getGameExecutor();

    /**
     * {@return a future that completion also waits on}
     */
    CompletableFuture<Unit> getAlsoWaitedFor();

    /**
     * {@return whether this reload is being profiled}
     */
    boolean isProfiled();
}
