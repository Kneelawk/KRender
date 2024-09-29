package com.kneelawk.krender.engine.base.mesh;

import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.mesh.Mesh;
import com.kneelawk.krender.engine.api.mesh.MeshBuilder;
import com.kneelawk.krender.engine.base.BaseKRendererApi;
import com.kneelawk.krender.engine.base.buffer.BaseQuadFormat;
import com.kneelawk.krender.engine.base.buffer.RootQuadEmitter;

/**
 * Base {@link MeshBuilder} implementation.
 */
public class BaseMeshBuilder implements MeshBuilder {
    private final Maker maker;
    /**
     * The renderer that this mesh builder, its emitter, and all built meshes will be associated with.
     */
    protected final BaseKRendererApi renderer;
    /**
     * This mesh builder's scratch data buffer.
     */
    protected int[] data = new int[256];
    /**
     * The current index within the scratch buffer.
     */
    protected int index = 0;
    /**
     * The current length of the scratch buffer.
     */
    protected int limit = data.length;

    /**
     * Creates a new base mesh builder.
     *
     * @param renderer the render that this mesh builder, its emitter, and all built meshes will be associated with.
     */
    public BaseMeshBuilder(BaseKRendererApi renderer) {
        this.maker = new Maker(renderer);
        this.renderer = renderer;
        maker.load(data, index);
        maker.clear();
    }

    /**
     * Makes sure this buffer and its associated buffer have enough capacity for another quad.
     *
     * @param stride the size of the next quad.
     */
    protected void ensureCapacity(int stride) {
        if (stride > limit - index) {
            limit *= 2;
            final int[] bigger = new int[limit];
            System.arraycopy(data, 0, bigger, 0, index);
            data = bigger;
            maker.begin(data, index);
        }
    }

    @Override
    public QuadEmitter emitter() {
        maker.begin(data, index);
        return maker;
    }

    @Override
    public Mesh build() {
        final int[] packed = new int[index];
        System.arraycopy(data, 0, packed, 0, index);
        index = 0;
        maker.begin(data, index);
        return new BaseMesh(renderer, packed);
    }

    private class Maker extends RootQuadEmitter {
        public Maker(BaseKRendererApi renderer) {
            super(renderer);
        }

        @Override
        public void emitDirectly() {
            index += BaseQuadFormat.TOTAL_STRIDE;
            ensureCapacity(BaseQuadFormat.TOTAL_STRIDE);
            baseIndex = index;
        }
    }
}
