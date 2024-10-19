package com.kneelawk.krender.model.obj.impl;

import java.util.concurrent.CompletableFuture;

import com.kneelawk.krender.model.loading.api.ModelBakeryPlugin;

public class KObj {
    public static void register() {
        ModelBakeryPlugin.registerPreparable((resourceManager, prepareExecutor) -> CompletableFuture.supplyAsync(() -> {
            return "hello";
        }, prepareExecutor), (resource, ctx) -> {

        });
    }
}
