package com.kneelawk.krender.engine.api.backend;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BooleanSupplier;

import com.kneelawk.commonevents.api.Event;
import com.kneelawk.krender.engine.impl.KRELog;
import com.kneelawk.krender.engine.impl.Platform;

/**
 * Callback for registering backends.
 */
public interface BackendRegistrationCallback {

    /**
     * Event for this callback.
     * <p>
     * You can listen for this callback via registering directly or via Common Events' mod-scanning.
     */
    Event<BackendRegistrationCallback> EVENT = Event.create(BackendRegistrationCallback.class, callbacks -> ctx -> {
        for (BackendRegistrationCallback callback : callbacks) {
            try {
                callback.registerBackends(ctx);
            } catch (Exception e) {
                KRELog.LOG.error("Error invoking backend registration callback", e);
            }
        }
    });

    /**
     * Called on a registration listener to allow it to register one or more backends.
     *
     * @param ctx the context to register the listeners to.
     */
    void registerBackends(Context ctx);

    /**
     * Context supplied to backend registration listeners.
     */
    interface Context {
        /**
         * Registers the given backend instance.
         *
         * @param backend the backend instance to register.
         */
        void registerBackend(KRenderBackend backend);

        /**
         * Registers the given backend by its class name.
         * <p>
         * The specified class must have a public no-arguments constructor.
         *
         * @param className the name of the backend implementation class. this must implement {@link KRenderBackend}.
         */
        default void registerBackend(String className) {
            KRenderBackend backend;
            try {
                backend = (KRenderBackend) Class.forName(className).getConstructor().newInstance();
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException("Unable to load KRender backend: " + className, e);
            }
            registerBackend(backend);
        }

        /**
         * Registers the given backend by its class name, checking a predicate first.
         * <p>
         * The specified classes must have public no-arguments constructors.
         *
         * @param predicateClassName the name of the backend predicate class. This must implement {@link BooleanSupplier}.
         * @param backendClassName   the name of the backend implementation class. This must implement {@link KRenderBackend}.
         */
        default void registerBackend(String predicateClassName, String backendClassName) {
            BooleanSupplier predicate;
            try {
                predicate = (BooleanSupplier) Class.forName(predicateClassName).getConstructor().newInstance();
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException("Unable to load KRender backend predicate: " + predicateClassName, e);
            }

            if (!predicate.getAsBoolean()) return;

            KRenderBackend backend;
            try {
                backend = (KRenderBackend) Class.forName(backendClassName).getConstructor().newInstance();
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException("Unable to load KRender backend: " + backendClassName, e);
            }
            registerBackend(backend);
        }

        /**
         * Convenience method to check if the given mod exists on the current platform.
         *
         * @param modId the mod-id of the mod to check for.
         * @return whether the given mod is present in this game instance.
         */
        default boolean isModLoaded(String modId) {
            return Platform.INSTANCE.isModLoaded(modId);
        }
    }
}
