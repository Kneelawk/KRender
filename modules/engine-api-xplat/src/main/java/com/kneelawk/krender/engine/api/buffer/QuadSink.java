package com.kneelawk.krender.engine.api.buffer;

import net.minecraft.client.renderer.MultiBufferSource;

/**
 * Universal interface for API-users to send vertex data to a backend.
 * <p>
 * This supports both the per-quad style of the Fabric Render API and the per-vertex style of vanilla.
 */
public interface QuadSink extends MultiBufferSource {
    /**
     * {@return a view of this quad sink as a per-quad model-data emitter}
     */
    QuadEmitter asQuadEmitter();
}
