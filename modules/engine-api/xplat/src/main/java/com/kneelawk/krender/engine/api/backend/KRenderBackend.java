package com.kneelawk.krender.engine.api.backend;

import com.kneelawk.krender.engine.api.KRenderer;

/**
 * Provides access to a {@link KRenderer}.
 */
public interface KRenderBackend {
    /**
     * {@return this backend's renderer}
     */
    KRenderer getRenderer();

    /**
     * Gets this backend's name.
     * <p>
     * Backend names allow users and mod authors to select which backend they would prefer to use instead of the
     * default one.
     *
     * @return this backend's name
     */
    String getName();

    /**
     * Gets this backend's priority.
     * <p>
     * Backends with lower integer values represent higher priorities and will be selected over backends with higher
     * integer values.
     *
     * @return this backend's priority
     */
    default int getPriority() {
        return 0;
    }
}
