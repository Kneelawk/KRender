package com.kneelawk.krender.engine.backend.test.impl;

import java.util.List;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import com.kneelawk.krender.engine.api.model.BakedModelCore;
import com.kneelawk.krender.engine.base.model.BakedModelCoreProvider;

public class TestBakedModel implements BakedModel, BakedModelCoreProvider {
    private final BakedModelCore<?> core;

    public TestBakedModel(BakedModelCore<?> core) {
        this.core = core;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
        // returns nothing, because this is usually outside the bounds of testing
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
    public TextureAtlasSprite getParticleIcon() {
        return core.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return core.getTransforms();
    }

    @Override
    public BakedModelCore<?> krender$getCore() {
        return core;
    }
}
