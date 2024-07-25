package com.kneelawk.krender.model.creation.api;

import net.minecraft.client.resources.model.BakedModel;

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
    public BakedModel wrap(BakedModelCore core) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Creates a new {@link BakedModelFactory} for use in creating {@link BakedModel}s.
     *
     * @return the new {@link BakedModelFactory}.
     */
    public static BakedModelFactory create() {
        return new BakedModelFactory();
    }
}
