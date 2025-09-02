package com.kneelawk.krender.model.loading.impl.mixin.api;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;

public interface Duck_ModelManager {
    BlockStateModel krender$getExtraModel(ResourceLocation path);
}
