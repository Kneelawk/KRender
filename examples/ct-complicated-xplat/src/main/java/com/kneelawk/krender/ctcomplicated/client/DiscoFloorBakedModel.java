package com.kneelawk.krender.ctcomplicated.client;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.TriState;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.material.BlendMode;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.api.model.BakedModelCore;
import com.kneelawk.krender.engine.api.model.ModelBlockContext;

public record DiscoFloorBakedModel(TextureAtlasSprite particle, TextureAtlasSprite[] baseSprites,
                                   TextureAtlasSprite[] glowingSprites) implements BakedModelCore<CTUtils.Data> {
    private static final RenderMaterial BASE_MATERIAL =
        KRenderer.getDefault().materialManager().materialFinder().setBlendMode(BlendMode.CUTOUT).find();
    private static final RenderMaterial GLOW_MATERIAL =
        KRenderer.getDefault().materialManager().materialFinder().setBlendMode(BlendMode.CUTOUT).setEmissive(true)
            .setDiffuseDisabled(true).setAmbientOcclusionMode(TriState.FALSE).find();

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particle;
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public CTUtils.Data getBlockKey(ModelBlockContext ctx) {
        return CTUtils.getData(true, true, ctx);
    }

    @Override
    public void renderBlock(QuadEmitter renderTo, CTUtils.Data blockKey) {
        CTUtils.render(BASE_MATERIAL, baseSprites, renderTo, blockKey);
        CTUtils.render(GLOW_MATERIAL, glowingSprites, renderTo, blockKey);
    }
}
