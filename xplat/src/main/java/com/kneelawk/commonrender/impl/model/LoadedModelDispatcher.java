package com.kneelawk.commonrender.impl.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class LoadedModelDispatcher {
    // reload is only executed once at a time, usually on the render thread, so this should be safe
    public static AtomicReference<CompletableFuture<LoadedModelDispatcher>> DISPATCHER_FUTURE = new AtomicReference<>();
    
    public static void collectModelLoaders() {
        // TODO
        DISPATCHER_FUTURE.set(CompletableFuture.completedFuture(new LoadedModelDispatcher()));
    }
}
