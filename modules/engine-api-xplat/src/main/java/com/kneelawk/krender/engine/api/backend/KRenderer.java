package com.kneelawk.krender.engine.api.backend;

import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.model.BakedModelFactory;

/**
 * Describes an implementation of the KRender Engine API.
 */
public interface KRenderer {

    /**
     * This gets or creates a baked model factory for this backend.
     *
     * @return the requested baked model factory.
     */
    BakedModelFactory bakedModelFactory();

    /**
     * {@return this backend's material manager}
     */
    MaterialManager materialManager();
}
