package com.kneelawk.commonrender.impl.loading;

import java.util.List;

import com.kneelawk.commonrender.api.loading.ModelBakeryPlugin;
import com.kneelawk.commonrender.impl.CRLog;

public record PreparedModelBakeryPluginList(List<ModelBakeryPlugin> plugins,
                                            List<? extends PreparedModelBakeryPlugin<?>> preparedPlugins) {
    public ModelBakeryPluginContext loadPlugins() {
        ModelBakeryPluginContext ctx = new ModelBakeryPluginContext();

        for (ModelBakeryPlugin plugin : plugins) {
            try {
                plugin.init(ctx);
            } catch (Exception e) {
                CRLog.LOGGER.error("Error initializing model bakery plugin", e);
            }
        }

        for (PreparedModelBakeryPlugin<?> plugin : preparedPlugins) {
            try {
                plugin.init(ctx);
            } catch (Exception e) {
                CRLog.LOGGER.error("Error initializing model bakery plugin", e);
            }
        }

        return ctx;
    }
}
