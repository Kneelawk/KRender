package com.kneelawk.krender.impl.loading;

import java.util.List;

import com.kneelawk.krender.api.loading.ModelBakeryPlugin;
import com.kneelawk.krender.impl.KRLog;

public record PreparedModelBakeryPluginList(List<ModelBakeryPlugin> plugins,
                                            List<? extends PreparedModelBakeryPlugin<?>> preparedPlugins) {
    public ModelBakeryPluginContext loadPlugins() {
        ModelBakeryPluginContext ctx = new ModelBakeryPluginContext();

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
