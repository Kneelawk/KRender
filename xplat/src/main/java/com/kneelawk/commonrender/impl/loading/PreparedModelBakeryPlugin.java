package com.kneelawk.commonrender.impl.loading;

import com.kneelawk.commonrender.api.loading.ModelBakeryPlugin;
import com.kneelawk.commonrender.api.loading.PreparableModelBakeryPlugin;
import com.kneelawk.commonrender.impl.CRLog;

public record PreparedModelBakeryPlugin<T>(T resource, PreparableModelBakeryPlugin<T> plugin) {
    void init(ModelBakeryPlugin.Context ctx) {
        try {
            plugin.init(resource, ctx);
        } catch (Exception e) {
            CRLog.LOGGER.error("Error loading model bakery plugin", e);
        }
    }
}
