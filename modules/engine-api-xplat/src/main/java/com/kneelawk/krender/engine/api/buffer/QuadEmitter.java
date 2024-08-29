package com.kneelawk.krender.engine.api.buffer;

/**
 * Per-quad Fabric Render API style model emitter.
 */
public interface QuadEmitter extends QuadView, QuadSink {
    /**
     * Finishes one quad, emitting it to the backend and moving on to the next quad.
     *
     * @return this quad emitter.
     */
    QuadEmitter emit();

    @Override
    default QuadEmitter asQuadEmitter() {
        return this;
    }
}
