package com.kneelawk.krender.engine.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

import com.kneelawk.krender.engine.api.KRenderer;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.api.mesh.Mesh;
import com.kneelawk.krender.engine.api.mesh.MeshBuilder;
import com.kneelawk.krender.engine.api.model.StaticBlockStateModelCore;
import com.kneelawk.krender.engine.api.util.DirectionIds;

public class VanillaAdapterModelCore implements StaticBlockStateModelCore {
    private final BlockStateModel model;
    private final AtomicReference<Mesh> mesh = new AtomicReference<>(null);

    public VanillaAdapterModelCore(BlockStateModel model) {this.model = model;}

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return model.particleIcon();
    }

    @Override
    public Mesh getMesh() {
        if (mesh.get() == null) {
            RenderMaterial material = KRenderer.getDefault().materialManager().defaultMaterial();
            MeshBuilder builder = KRenderer.getDefault().meshBuilder();
            QuadEmitter emitter = builder.emitter();
            RandomSource random = RandomSource.create(42);
            List<BlockModelPart> parts = model.collectParts(random);

            for (int i = 0; i < DirectionIds.DIRECTION_COUNT; i++) {
                Direction cullFace = DirectionIds.idToDirection(i);

                random.setSeed(42);

                for (BlockModelPart part : parts) {
                    for (BakedQuad quad : part.getQuads(cullFace)) {
                        emitter.fromVanilla(quad, material, cullFace);
                        emitter.emit();
                    }
                }
            }

            Mesh mesh = builder.build();

            if (this.mesh.compareAndSet(null, mesh)) {
                return mesh;
            }
        }

        return mesh.get();
    }
}
