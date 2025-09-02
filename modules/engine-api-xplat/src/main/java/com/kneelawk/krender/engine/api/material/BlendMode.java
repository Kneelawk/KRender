package com.kneelawk.krender.engine.api.material;

import net.minecraft.client.renderer.RenderPipelines;

import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.StringRepresentable;

/**
 * Describes how materials are blended.
 */
public enum BlendMode implements StringRepresentable {
    /**
     * Uses what ever blend mode is defined for the block being rendered.
     *
     * @see ItemBlockRenderTypes
     */
    DEFAULT(null),

    /**
     * Materials with this blend mode do not blend, being entirely opaque.
     *
     * @see RenderType#solid()
     */
    SOLID(ChunkSectionLayer.SOLID),

    /**
     * Materials with this blend mode cause pixels to only be present if they are greater than half alpha. This version
     * also uses mip-mapping. Leaves would be a good example of a block using this blend mode.
     *
     * @see RenderType#cutoutMipped()
     */
    CUTOUT_MIPPED(ChunkSectionLayer.CUTOUT_MIPPED),

    /**
     * Materials with this blend mode cause pixels to only be present if they are greater than half alpha. This version
     * does not use mip-mapping. This blend mode is good for textures with hard edges that need to remain clear from
     * a distance.
     *
     * @see RenderType#cutout()
     */
    CUTOUT(ChunkSectionLayer.CUTOUT),

    /**
     * Materials with this blend mode blend pixels with those behind them based on the pixels' alpha.
     *
     * @see RenderPipelines#TRANSLUCENT
     */
    TRANSLUCENT(ChunkSectionLayer.TRANSLUCENT);

    /**
     * Blend mode codec.
     */
    public static final Codec<BlendMode> CODEC = StringRepresentable.fromEnum(BlendMode::values);

    /**
     * The chunk section layer associated with this blend mode.
     */
    public final ChunkSectionLayer chunkSectionLayer;

    BlendMode(@Nullable ChunkSectionLayer chunkSectionLayer) {
        this.chunkSectionLayer = chunkSectionLayer;
    }

    @Override
    public @NotNull String getSerializedName() {
        return switch (this) {
            case DEFAULT -> "default";
            case SOLID -> "solid";
            case CUTOUT_MIPPED -> "cutout_mipped";
            case CUTOUT -> "cutout";
            case TRANSLUCENT -> "translucent";
        };
    }
}
