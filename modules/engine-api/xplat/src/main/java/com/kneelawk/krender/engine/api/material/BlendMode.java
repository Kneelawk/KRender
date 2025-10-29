package com.kneelawk.krender.engine.api.material;

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
    SOLID(RenderType.solid()),

    /**
     * Materials with this blend mode cause pixels to only be present if they are greater than half alpha. This version
     * also uses mip-mapping. Leaves would be a good example of a block using this blend mode.
     *
     * @see RenderType#cutoutMipped()
     */
    CUTOUT_MIPPED(RenderType.cutoutMipped()),

    /**
     * Materials with this blend mode cause pixels to only be present if they are greater than half alpha. This version
     * does not use mip-mapping. This blend mode is good for textures with hard edges that need to remain clear from
     * a distance.
     *
     * @see RenderType#cutout()
     */
    CUTOUT(RenderType.cutout()),

    /**
     * Materials with this blend mode blend pixels with those behind them based on the pixels' alpha.
     *
     * @see RenderType#translucent()
     */
    TRANSLUCENT(RenderType.translucent());

    /**
     * Blend mode codec.
     */
    public static final Codec<BlendMode> CODEC = StringRepresentable.fromEnum(BlendMode::values);

    /**
     * The block render type associated with this blend mode.
     */
    public final RenderType blockRenderType;

    BlendMode(@Nullable RenderType blockRenderType) {
        this.blockRenderType = blockRenderType;
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
