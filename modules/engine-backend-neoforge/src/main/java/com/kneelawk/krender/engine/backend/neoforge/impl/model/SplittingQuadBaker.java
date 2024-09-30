package com.kneelawk.krender.engine.backend.neoforge.impl.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.Mth;

import com.kneelawk.krender.engine.api.texture.SpriteFinder;
import com.kneelawk.krender.engine.api.util.DirectionIds;
import com.kneelawk.krender.engine.backend.neoforge.impl.NFRenderer;
import com.kneelawk.krender.engine.base.buffer.RootQuadEmitter;

public class SplittingQuadBaker {
    private static final ThreadLocal<SplittingQuadBaker> POOL = ThreadLocal.withInitial(SplittingQuadBaker::new);

    private static final RenderType[] RENDER_TYPES =
        RenderType.CHUNK_BUFFER_LAYERS.toArray(new RenderType[RenderType.CHUNK_BUFFER_LAYERS.size() + 1]);
    private static final int RENDER_TYPE_COUNT = RENDER_TYPES.length;
    private static final int DIRECTION_COUNT = DirectionIds.DIRECTION_COUNT;

    private static final int RENDER_TYPE_SHIFT = 0;
    private static final int RENDER_TYPE_BIT_COUNT = Mth.ceillog2(RENDER_TYPE_COUNT);
    private static final int RENDER_TYPE_FULL_COUNT = 1 << RENDER_TYPE_BIT_COUNT;
    private static final int DIRECTION_SHIFT = RENDER_TYPE_SHIFT + RENDER_TYPE_BIT_COUNT;
    private static final int DIRECTION_ID_MASK = DirectionIds.DIRECTION_MASK;
    private static final int TOTAL_COUNT = RENDER_TYPE_FULL_COUNT * DIRECTION_COUNT;

    private static final Scratch[] EMPTY = new Scratch[TOTAL_COUNT];

    private final Maker maker = new Maker();
    private final SpriteFinder finder =
        SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS));

    private final Scratch[] datas = new Scratch[TOTAL_COUNT];

    public void clear() {
        maker.clear();
        System.arraycopy(EMPTY, 0, datas, 0, TOTAL_COUNT);
    }

    public Result bake() {

    }

    private class Maker extends RootQuadEmitter {
        public Maker() {
            super(NFRenderer.INSTANCE);

        }

        @Override
        public void emitDirectly() {

        }
    }

    private class Scratch {
        private int[] data = new int[256];
        
    }

    public static class Result {
        private int[][] datas;
    }
}
