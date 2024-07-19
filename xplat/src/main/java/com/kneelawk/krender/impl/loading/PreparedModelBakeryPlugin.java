package com.kneelawk.krender.impl.loading;

import com.kneelawk.krender.api.loading.ModelBakeryPlugin;
import com.kneelawk.krender.api.loading.PreparableModelBakeryPlugin;
import com.kneelawk.krender.impl.KRLog;

public record PreparedModelBakeryPlugin<T>(T resource, PreparableModelBakeryPlugin<T> plugin) {
    void init(ModelBakeryPlugin.Context ctx) {
        try {
            plugin.init(resource, ctx);
        } catch (Exception e) {
            KRLog.LOGGER.error("Error loading model bakery plugin", e);
        }
    }
}
