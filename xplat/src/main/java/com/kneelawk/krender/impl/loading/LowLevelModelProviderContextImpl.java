package com.kneelawk.krender.impl.loading;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.api.loading.LowLevelModelProvider;

public class LowLevelModelProviderContextImpl implements LowLevelModelProvider.Context {
    private final ResourceLocation location;
    private final ModelBakery bakery;

    public LowLevelModelProviderContextImpl(ResourceLocation location, ModelBakery bakery) {
        this.location = location;
        this.bakery = bakery;
    }

    @Override
    public ResourceLocation location() {return location;}

    @Override
    public ModelBakery bakery() {return bakery;}
}
