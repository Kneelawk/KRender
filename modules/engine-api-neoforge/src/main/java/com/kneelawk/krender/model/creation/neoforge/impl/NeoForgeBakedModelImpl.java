package com.kneelawk.krender.model.creation.neoforge.impl;

import java.util.List;

import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import com.kneelawk.krender.model.creation.api.BakedModelCore;

public class NeoForgeBakedModelImpl implements BakedModel {
    private final BakedModelCore core;

    public NeoForgeBakedModelImpl(BakedModelCore core) {this.core = core;}

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction,
                                    RandomSource randomSource) {
        return List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return core.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return core.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return core.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return core.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return core.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return core.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return core.getOverrides();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                    ModelData data, @Nullable RenderType renderType) {
        // TODO
        return BakedModel.super.getQuads(state, side, rand, data, renderType);
    }

    @Override
    public TriState useAmbientOcclusion(BlockState state, ModelData data, RenderType renderType) {
        // default impl for now
        return BakedModel.super.useAmbientOcclusion(state, data, renderType);
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack,
                                     boolean applyLeftHandTransform) {
        // default impl for now
        return BakedModel.super.applyTransform(transformType, poseStack, applyLeftHandTransform);
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        // TODO
        return BakedModel.super.getModelData(level, pos, state, modelData);
    }

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        // default impl for now
        return BakedModel.super.getParticleIcon(data);
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        // TODO
        return BakedModel.super.getRenderTypes(state, rand, data);
    }

    @Override
    public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous) {
        // TODO
        return BakedModel.super.getRenderTypes(itemStack, fabulous);
    }

    @Override
    public List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
        // default impl for now
        return BakedModel.super.getRenderPasses(itemStack, fabulous);
    }
}
