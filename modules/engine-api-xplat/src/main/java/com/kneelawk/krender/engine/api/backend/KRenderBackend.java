package com.kneelawk.krender.engine.api.backend;

/**
 * Provides access to a {@link KRenderer}.
 */
public interface KRenderBackend {
    /**
     * {@return this backend's renderer}
     */
    KRenderer getRenderer();

    /**
     * {@return this backend's priority}
     */
    default int getPriority() {
        return 0;
    }
}
