package com.kneelawk.commonrender.api.loading;

import org.jetbrains.annotations.ApiStatus;

import com.kneelawk.commonevents.api.Event;

/**
 * Used to register {@link ModelBakeryPlugin}s and {@link PreparableModelBakeryPlugin}s.
 */
@FunctionalInterface
public interface ModelBakeryPluginCallback {

    /**
     * Event fired when the {@link net.minecraft.client.resources.model.ModelBakery} is first being loaded.
     * <p>
     * Registered plugins will be cached for later loads.
     */
    Event<ModelBakeryPluginCallback> EVENT = Event.create(ModelBakeryPluginCallback.class, callbacks -> event -> {
        for (ModelBakeryPluginCallback callback : callbacks) {
            callback.modelBakeryLoading(event);
        }
    });

    /**
     * Called when the {@link net.minecraft.client.resources.model.ModelBakery} is loading, and it is time to add plugins.
     *
     * @param ctx the event to allow for the registration of model bakery plugins.
     */
    void modelBakeryLoading(Context ctx);

    /**
     * The event object given to the bakery-loading callback.
     */
    @ApiStatus.NonExtendable
    interface Context {
        /**
         * Use to register a simple {@link ModelBakeryPlugin}.
         *
         * @param plugin the plugin to register.
         */
        void register(ModelBakeryPlugin plugin);

        /**
         * Used to register a {@link PreparableModelBakeryPlugin} that loads resources before registering objects.
         *
         * @param loader the resource loader.
         * @param plugin the plugin that registers objects.
         * @param <T>    the type of resource that the plugin loads.
         */
        <T> void registerPreparable(PreparableModelBakeryPlugin.ResourceLoader<T> loader,
                                    PreparableModelBakeryPlugin<T> plugin);
    }
}
