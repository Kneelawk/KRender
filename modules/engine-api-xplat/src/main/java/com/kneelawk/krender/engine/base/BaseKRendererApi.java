package com.kneelawk.krender.engine.base;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.base.material.BaseMaterialManagerApi;

/**
 * Exposes base-specific renderer api that renderers should implement in order to be compatible with base implementations.
 */
public interface BaseKRendererApi extends KRenderer {
    @Override
    BaseMaterialManagerApi<?> materialManager();
}
