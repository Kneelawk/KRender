package com.kneelawk.krender.engine.base.model;

import com.kneelawk.krender.engine.api.model.BakedModelCore;

/**
 * Implemented by objects that either contain or can be converted into a {@link BakedModelCore}.
 */
public interface BakedModelCoreProvider {
    /**
     * {@return the core that this model either contains or represents}
     * <p>
     * Note: This method is prefixed with a mod id to prevent collisions when implemented by mixins.
     */
    BakedModelCore<?> krender$getCore();
}
