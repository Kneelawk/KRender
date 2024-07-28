package com.kneelawk.krender.model.creation.api;

import net.minecraft.client.resources.model.BakedModel;

import com.kneelawk.krender.model.creation.impl.Platform;

/**
 * This class is responsible for wrapping a {@link BakedModelCore} and creating a {@link BakedModel}.
 */
public class BakedModelFactory {
    private BakedModelFactory() {}

    /**
     * Creates a {@link BakedModel} by wrapping a {@link BakedModelCore} in a platform-dependent {@link BakedModel}
     * implementation, allowing for better integration with NeoForge and FRAPI's own interfaces.
     *
     * @param core the core to wrap.
     * @return the created baked model.
     */
    public static BakedModel wrap(BakedModelCore core) {
        return Platform.INSTANCE.wrap(core);
    }
}
