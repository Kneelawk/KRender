package com.kneelawk.krender.engine.api.model;

import net.minecraft.client.resources.model.BakedModel;

/**
 * Responsible for wrapping a {@link BakedModelCore} and creating a {@link BakedModel}.
 */
public interface BakedModelFactory {
    /**
     * Creates a {@link BakedModel} by wrapping a {@link BakedModelCore} in a platform-dependent {@link BakedModel}
     * implementation, allowing for better integration with NeoForge and FRAPI's own interfaces.
     *
     * @param core the core to wrap.
     * @return the created baked model.
     */
    BakedModel wrap(BakedModelCore core);
}
