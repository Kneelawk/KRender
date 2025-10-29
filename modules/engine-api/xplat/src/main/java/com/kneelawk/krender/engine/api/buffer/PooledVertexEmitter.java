package com.kneelawk.krender.engine.api.buffer;

/**
 * A vertex emitter that must be returned to its pool once it is no-longer needed.
 * <p>
 * This vertex emitter can be returned to its pool via {@link #close()}.
 */
public interface PooledVertexEmitter extends VertexEmitter, AutoCloseable {
    @Override
    void close();
}
