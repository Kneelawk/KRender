package com.kneelawk.krender.model.loading.impl.loading;

import java.util.List;

import com.kneelawk.krender.model.loading.api.ModelBakeryPlugin;
import com.kneelawk.krender.model.loading.impl.KRLog;

public record PreparedModelBakeryPluginList(List<ModelBakeryPlugin> plugins,
                                            List<? extends PreparedModelBakeryPlugin<?>> preparedPlugins) {
    public ModelBakeryPluginContextImpl loadPlugins() {
        ModelBakeryPluginContextImpl ctx = new ModelBakeryPluginContextImpl();

        for (ModelBakeryPlugin plugin : plugins) {
            try {
                plugin.init(ctx);
            } catch (Exception e) {
                KRLog.LOGGER.error("Error initializing model bakery plugin", e);
            }
        }

        for (PreparedModelBakeryPlugin<?> plugin : preparedPlugins) {
            try {
                plugin.init(ctx);
            } catch (Exception e) {
                KRLog.LOGGER.error("Error initializing model bakery plugin", e);
            }
        }

        return ctx;
    }
}
