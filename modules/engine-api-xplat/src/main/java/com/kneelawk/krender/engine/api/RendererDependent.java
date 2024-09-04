package com.kneelawk.krender.engine.api;

import org.jetbrains.annotations.Nullable;

/**
 * Implemented by any type that is supplied by a render engine backend.
 */
public interface RendererDependent {
    /**
     * Gets the renderer that this object is supplied by.
     *
     * @return the renderer that this type is associated with, or {@code null} to indicate that it is not associated
     * with any specific renderer and is therefore compatible with all renderers.
     */
    @Nullable KRenderer getRenderer();

    /**
     * Gets the renderer that this object is supplied by, or the default renderer if this object is not supplied by any
     * specific renderer.
     *
     * @return the renderer that this type is associated with, or {@link KRenderer#getDefault()} if this is not
     * associated with any specific renderer.
     */
    default KRenderer getRendererOrDefault() {
        KRenderer renderer = getRenderer();
        if (renderer == null) return KRenderer.getDefault();
        return renderer;
    }
}
