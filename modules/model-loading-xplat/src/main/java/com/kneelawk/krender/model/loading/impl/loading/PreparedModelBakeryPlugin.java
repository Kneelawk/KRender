package com.kneelawk.krender.model.loading.impl.loading;

import com.kneelawk.krender.model.loading.api.ModelBakeryPlugin;
import com.kneelawk.krender.model.loading.api.PreparableModelBakeryPlugin;
import com.kneelawk.krender.model.loading.impl.KRLog;

public record PreparedModelBakeryPlugin<T>(T resource, PreparableModelBakeryPlugin<T> plugin) {
    void init(ModelBakeryPlugin.Context ctx) {
        try {
            plugin.init(resource, ctx);
        } catch (Exception e) {
            KRLog.LOGGER.error("Error loading model bakery plugin", e);
        }
    }
}
