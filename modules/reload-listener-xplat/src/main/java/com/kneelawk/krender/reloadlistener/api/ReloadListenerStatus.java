package com.kneelawk.krender.reloadlistener.api;

import com.kneelawk.krender.reloadlistener.impl.ReloadListenerStatusImpl;

/**
 * Provides access to the current status of a resource reload.
 */
public final class ReloadListenerStatus {
    private ReloadListenerStatus() {}

    /**
     * {@return whether there is currently a resource-pack reload operation occurring}
     */
    public static boolean isReloadingResourcePacks() {
        return ReloadListenerStatusImpl.resourcePackReloading.get();
    }

    /**
     * {@return whether there is currently a data-pack reload operation occurring}
     */
    public static boolean isReloadingDataPacks() {
        return ReloadListenerStatusImpl.dataPackReloading.get();
    }
}
