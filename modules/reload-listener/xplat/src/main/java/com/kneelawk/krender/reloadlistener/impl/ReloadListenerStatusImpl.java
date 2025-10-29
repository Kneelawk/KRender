package com.kneelawk.krender.reloadlistener.impl;

import java.util.concurrent.atomic.AtomicBoolean;

public class ReloadListenerStatusImpl {
    public static AtomicBoolean resourcePackReloading = new AtomicBoolean(false);
    public static AtomicBoolean dataPackReloading = new AtomicBoolean(false);
}
