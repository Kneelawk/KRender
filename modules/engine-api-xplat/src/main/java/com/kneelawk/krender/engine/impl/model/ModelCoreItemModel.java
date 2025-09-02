package com.kneelawk.krender.engine.impl.model;

import net.minecraft.client.renderer.block.model.BlockStateModel;

import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;

import net.minecraft.client.resources.model.BlockModelRotation;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import com.kneelawk.krender.engine.base.model.BakedModelCoreProvider;

public class ModelCoreItemModel implements ItemModel {
    private final BlockStateModel model;

    public ModelCoreItemModel(BlockStateModel model) {
        this.model = model;
    }

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver,
                       ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable LivingEntity entity,
                       int seed) {
        ItemStackRenderState.LayerRenderState layer = renderState.newLayer();

        if (model instanceof BakedModelCoreProvider provider) {
            layer.setupSpecialModel(ModelCoreSpecialRenderer.INSTANCE,
                new ModelCoreSpecialRenderer.Input(provider.krender$getCore(), stack));
        }
    }

    public record Unbaked(ResourceLocation model) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC =
            ResourceLocation.CODEC.fieldOf("model").xmap(Unbaked::new, Unbaked::model);

        @Override
        public MapCodec<? extends ItemModel.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext context) {
            BlockStateModel bakedModel = new SingleVariant(SimpleModelWrapper.bake(context.blockModelBaker(), model,
                    BlockModelRotation.X0_Y0));
            return new ModelCoreItemModel(bakedModel);
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.markDependency(model);
        }
    }
}
