package com.kneelawk.krender.model.loading.impl.loading;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class DelegatingBlockStateModel implements BlockStateModel.UnbakedRoot {
    private final ResourceLocation reference;

    public DelegatingBlockStateModel(ResourceLocation reference) {this.reference = reference;}

    @Override
    public BlockStateModel bake(BlockState state, ModelBaker baker) {
        return new SingleVariant(SimpleModelWrapper.bake(baker, reference, BlockModelRotation.X0_Y0));
    }
    @Override
    public void resolveDependencies(Resolver resolver) {
        resolver.markDependency(reference);
    }

    @Override
    public Object visualEqualityGroup(BlockState state) {
        return this;
    }

}
