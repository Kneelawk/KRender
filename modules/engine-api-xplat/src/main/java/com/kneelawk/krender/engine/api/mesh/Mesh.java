package com.kneelawk.krender.engine.api.mesh;

import java.util.function.Consumer;

import com.kneelawk.krender.engine.api.RendererDependent;
import com.kneelawk.krender.engine.api.buffer.QuadEmitter;
import com.kneelawk.krender.engine.api.buffer.QuadSink;
import com.kneelawk.krender.engine.api.buffer.QuadView;

/**
 * A list of quads similar to a {@code List<BakedQuad>} except allowing backends to optimize format for performance
 * or memory footprint.
 */
public interface Mesh extends RendererDependent {
    /**
     * Use to access the quads in this mesh.
     * <p>
     * Note: Quads provided to the consumer will likely be re-used and should not be retained by the consumer.
     *
     * @param consumer the consumer of quads.
     */
    void forEach(Consumer<QuadView> consumer);

    /**
     * Outputs all quads to a quad emitter.
     *
     * @param emitter the emitter to emit all quads to.
     */
    default void outputTo(QuadEmitter emitter) {
        forEach(quad -> {
            quad.copyTo(emitter);
            emitter.emit();
        });
    }

    /**
     * Outputs all quads to a quad sink.
     *
     * @param sink the sink to emit all quads to.
     */
    default void outputTo(QuadSink sink) {
        outputTo(sink.asQuadEmitter());
    }
}
