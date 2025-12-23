package com.kneelawk.krender.engine.impl.model;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import com.kneelawk.krender.engine.base.model.BakedModelCoreProvider;

public class ModelCoreItemModel implements ItemModel {
    private final BakedModel model;

    public ModelCoreItemModel(BakedModel model) {
        this.model = model;
    }

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver,
                       ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable LivingEntity entity,
                       int seed) {
        ItemStackRenderState.LayerRenderState layer = renderState.newLayer();

        if (model instanceof BakedModelCoreProvider provider) {
            layer.setupSpecialModel(ModelCoreSpecialRenderer.INSTANCE,
                new ModelCoreSpecialRenderer.Input(provider.krender$getCore(), stack), model);
        }
    }

    public record Unbaked(Identifier model) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC =
            Identifier.CODEC.fieldOf("model").xmap(Unbaked::new, Unbaked::model);

        @Override
        public MapCodec<? extends ItemModel.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext context) {
            BakedModel bakedModel = context.bake(model);
            return new ModelCoreItemModel(bakedModel);
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.resolve(model);
        }
    }
}
