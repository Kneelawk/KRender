package com.kneelawk.krender.engine.api.mesh;

import com.kneelawk.krender.engine.api.RendererDependent;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;

/**
 * Builds a static mesh that can store geometry for later use.
 */
public interface MeshBuilder extends RendererDependent {
    /**
     * Gets a {@link QuadEmitter} that can be used to append the current mesh.
     * <p>
     * Calling this method a second time invalidates any non-emitted quads.
     * <p>
     * Only retain a returned emitter during the period of building a mesh.
     *
     * @return a quad emitter for appending to the current mesh.
     */
    QuadEmitter emitter();

    /**
     * Builds a mesh with all the quads currently in this mesh builder.
     *
     * @return the built mesh.
     */
    Mesh build();
}
