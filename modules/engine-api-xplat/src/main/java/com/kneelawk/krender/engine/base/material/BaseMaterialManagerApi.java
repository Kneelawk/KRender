package com.kneelawk.krender.engine.base.material;

import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.material.RenderMaterial;

/**
 * Exposes base-specific material manager api that managers should implement in order to be compatible with other base
 * implementations.
 *
 * @param <M> the type of render material managed by this material manager.
 */
public interface BaseMaterialManagerApi<M extends BaseMaterialViewApi & RenderMaterial> extends MaterialManager {
    /**
     * Gets a material when given the associated bits.
     *
     * @param bits the material's bits.
     * @return the associated material.
     */
    M getMaterialByBits(int bits);
}
