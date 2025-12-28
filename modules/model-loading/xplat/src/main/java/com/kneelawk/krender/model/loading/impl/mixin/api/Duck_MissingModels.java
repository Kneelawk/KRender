package com.kneelawk.krender.model.loading.impl.mixin.api;

import net.minecraft.client.resources.model.QuadCollection;

public interface Duck_MissingModels {
    void krender$setMissingModelQuads(QuadCollection collection);

    QuadCollection krender$getMissingModelQuads();
}
