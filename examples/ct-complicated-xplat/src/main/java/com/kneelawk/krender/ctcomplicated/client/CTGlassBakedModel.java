package com.kneelawk.krender.ctcomplicated.client;

import java.util.Arrays;

import org.jetbrains.annotations.UnknownNullability;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import com.kneelawk.krender.ctcomplicated.CTLog;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.api.model.BakedModelCore;
import com.kneelawk.krender.engine.api.model.ModelBlockContext;

import static com.kneelawk.krender.ctcomplicated.client.TexDirectionUtils.texDown;
import static com.kneelawk.krender.ctcomplicated.client.TexDirectionUtils.texLeft;
import static com.kneelawk.krender.ctcomplicated.client.TexDirectionUtils.texRight;
import static com.kneelawk.krender.ctcomplicated.client.TexDirectionUtils.texUp;
import static java.lang.Math.abs;

public record CTGlassBakedModel(boolean doCorners, boolean interiorBorder, TextureAtlasSprite particle,
                                TextureAtlasSprite[] sprites, RenderMaterial material)
    implements BakedModelCore<CTGlassBakedModel.Data> {
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final FacePos[] FACES = {
        new FacePos(0.0f + 0f, 0.0f + 0f, 0.5f, 0.5f, 0f),
        new FacePos(0.5f, 0.0f + 0f, 1.0f - 0f, 0.5f, 0f),
        new FacePos(0.0f + 0f, 0.5f, 0.5f, 1.0f - 0f, 0f),
        new FacePos(0.5f, 0.5f, 1.0f - 0f, 1.0f - 0f, 0f)
    };

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
    public CTGlassBakedModel.@UnknownNullability Data getBlockKey(ModelBlockContext ctx) {
        int[] indices = new int[6];
        for (int i = 0; i < 6; i++) {
            indices[i] = getIndices(ctx.getLevel(), ctx.getState(), ctx.getPos(), DIRECTIONS[i]);
        }

        return new Data(indices);
    }

    @Override
    public void renderBlock(QuadEmitter renderTo, @UnknownNullability Data blockKey) {
        CTLog.LOG.info("Render block {}", blockKey);
        int[] indices = blockKey.indices();
        for (int i = 0; i < 6; i++) {
            int index = indices[i];
            Direction side = DIRECTIONS[i];

            for (int corner = 0; corner < 4; corner++) {
                TextureAtlasSprite sprite = sprites[(index >>> (corner * 3)) & 0x7];
                if (sprite == null) continue;

                FACES[corner].emit(renderTo, side);
                renderTo.spriteBake(sprite, QuadEmitter.BAKE_ROTATE_NONE);
                renderTo.setQuadColor(-1, -1, -1, -1);
                renderTo.setColorIndex(-1);
                renderTo.setMaterial(material);
                renderTo.emit();
            }
        }
    }

    private int getIndices(BlockAndTintGetter view, BlockState state, BlockPos pos, Direction normal) {
        int horizontals = getHorizontals(view, state, pos, normal);
        int verticals = getVerticals(view, state, pos, normal);
        int corners;
        if (doCorners) corners = getCorners(view, state, pos, normal) & horizontals & verticals;
        else corners = 0;

        return (corners << 2) | (horizontals ^ corners) | ((verticals ^ corners) << 1);
    }

    private int getHorizontals(BlockAndTintGetter view, BlockState state, BlockPos pos, Direction normal) {
        boolean right = canConnect(view, state, pos, normal, pos.relative(texRight(normal)));
        boolean left = canConnect(view, state, pos, normal, pos.relative(texLeft(normal)));

        return (left ? 0x41 : 0) | (right ? 0x208 : 0);
    }

    private int getVerticals(BlockAndTintGetter view, BlockState state, BlockPos pos, Direction normal) {
        boolean up = canConnect(view, state, pos, normal, pos.relative(texUp(normal)));
        boolean down = canConnect(view, state, pos, normal, pos.relative(texDown(normal)));

        return (down ? 0x9 : 0) | (up ? 0x240 : 0);
    }

    private int getCorners(BlockAndTintGetter view, BlockState state, BlockPos pos, Direction normal) {
        boolean bl = canConnect(view, state, pos, normal, pos.relative(texDown(normal)).relative(texLeft(normal)));
        boolean br = canConnect(view, state, pos, normal, pos.relative(texDown(normal)).relative(texRight(normal)));
        boolean tl = canConnect(view, state, pos, normal, pos.relative(texUp(normal)).relative(texLeft(normal)));
        boolean tr = canConnect(view, state, pos, normal, pos.relative(texUp(normal)).relative(texRight(normal)));

        return (bl ? 0x1 : 0) | (br ? 0x8 : 0) | (tl ? 0x40 : 0) | (tr ? 0x200 : 0);
    }

    private boolean canConnect(BlockAndTintGetter view, BlockState state, BlockPos pos, Direction normal,
                               BlockPos offsetPos) {
        BlockPos outPos = offsetPos.relative(normal);
        BlockState offsetState = view.getBlockState(offsetPos);
        BlockState outState = view.getBlockState(outPos);
        return state.equals(offsetState) &&
            (!interiorBorder || !state.equals(outState));
    }

    public record Data(int[] indices) {
        @Override
        public String toString() {
            return "Data{" +
                "indices=" + Arrays.toString(indices) +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Data data = (Data) o;
            return Arrays.equals(indices, data.indices);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(indices);
        }
    }

    private record FacePos(float left, float bottom, float right, float top, float depth) {
        void emit(QuadEmitter emitter, Direction face) {
            square(emitter, face, left, bottom, right, top, depth);
            emitter.setUv(0, left, 1f - top);
            emitter.setUv(1, left, 1f - bottom);
            emitter.setUv(2, right, 1f - bottom);
            emitter.setUv(3, right, 1f - top);
        }

        private static void square(QuadEmitter emitter, Direction face, float left, float bottom, float right,
                                   float top, float depth) {
            if (abs(depth) < 0.00001) {
                depth = 0;
                emitter.setCullFace(face);
            } else {
                emitter.setCullFace(null);
            }

            emitter.setNominalFace(face);

            switch (face) {
                case DOWN -> {
                    emitter.setPos(0, left, depth, top);
                    emitter.setPos(1, left, depth, bottom);
                    emitter.setPos(2, right, depth, bottom);
                    emitter.setPos(3, right, depth, top);
                }
                case UP -> {
                    depth = 1f - depth;
                    top = 1f - top;
                    bottom = 1f - bottom;
                    emitter.setPos(0, left, depth, top);
                    emitter.setPos(1, left, depth, bottom);
                    emitter.setPos(2, right, depth, bottom);
                    emitter.setPos(3, right, depth, top);
                }
                case NORTH -> {
                    left = 1f - left;
                    right = 1f - right;
                    emitter.setPos(0, left, top, depth);
                    emitter.setPos(1, left, bottom, depth);
                    emitter.setPos(2, right, bottom, depth);
                    emitter.setPos(3, right, top, depth);
                }
                case SOUTH -> {
                    depth = 1f - depth;
                    emitter.setPos(0, left, top, depth);
                    emitter.setPos(1, left, bottom, depth);
                    emitter.setPos(2, right, bottom, depth);
                    emitter.setPos(3, right, top, depth);
                }
                case WEST -> {
                    emitter.setPos(0, depth, top, left);
                    emitter.setPos(1, depth, bottom, left);
                    emitter.setPos(2, depth, bottom, right);
                    emitter.setPos(3, depth, top, right);
                }
                case EAST -> {
                    depth = 1f - depth;
                    left = 1f - left;
                    right = 1f - right;
                    emitter.setPos(0, depth, top, left);
                    emitter.setPos(1, depth, bottom, left);
                    emitter.setPos(2, depth, bottom, right);
                    emitter.setPos(3, depth, top, right);
                }
            }
        }
    }
}
