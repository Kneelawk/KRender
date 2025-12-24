package com.kneelawk.krender.model.loading.impl.mixin.api;

import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.resources.Identifier;

public interface Duck_ModelManager {
    QuadCollection krender$getExtraModel(Identifier path);
}
