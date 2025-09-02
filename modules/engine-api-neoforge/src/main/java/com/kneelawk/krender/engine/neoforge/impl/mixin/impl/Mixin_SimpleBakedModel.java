package com.kneelawk.krender.engine.neoforge.impl.mixin.impl;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.resources.model.SimpleBakedModel;

import com.kneelawk.krender.engine.api.model.BlockStateModelCore;
import com.kneelawk.krender.engine.base.model.BakedModelCoreProvider;
import com.kneelawk.krender.engine.neoforge.impl.SimpleAdapterModelCore;

@Mixin(SimpleBakedModel.class)
public class Mixin_SimpleBakedModel implements BakedModelCoreProvider {
    @Override
    public BlockStateModelCore<?> krender$getCore() {
        return new SimpleAdapterModelCore((SimpleBakedModel) (Object) this);
    }
}
