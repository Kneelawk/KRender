package com.kneelawk.krender.engine.api.buffer;

/**
 * A quad emitter that must be returned to its pool once it is no-longer needed.
 * <p>
 * This quad emitter can be returned to its pool via {@link #close()}.
 */
public interface PooledQuadEmitter extends QuadEmitter, AutoCloseable {
    @Override
    void close();
}
