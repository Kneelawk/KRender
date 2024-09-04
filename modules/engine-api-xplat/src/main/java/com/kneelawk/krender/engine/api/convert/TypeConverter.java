package com.kneelawk.krender.engine.api.convert;

import net.minecraft.client.renderer.RenderType;

import com.kneelawk.krender.engine.api.buffer.QuadSink;
import com.kneelawk.krender.engine.api.material.RenderMaterial;

/**
 * Converts between different backends' types and between this backend's types and vanilla types.
 */
public interface TypeConverter {
    /**
     * Makes a best-effort attempt to convert a {@link RenderType} to a {@link RenderMaterial} specific to this backend.
     *
     * @param renderType the vanilla render-type to be converted into a backend-specific render-material.
     * @return the closest render-material the backend has to offer.
     */
    RenderMaterial toRenderMaterial(RenderType renderType);

    /**
     * Makes a best-effort attempt to convert a {@link RenderMaterial} to a {@link RenderType}.
     *
     * @param material the backend-specific material to be converted to a vanilla render-type.
     * @return the vanilla render-type closest to the given material.
     */
    RenderType toRenderType(RenderMaterial material);

    /**
     * Converts a render material that is potentially from another backend into one from this backend.
     *
     * @param material the render material to convert.
     * @return the new, roughly equivalent render material that is associated with this backend.
     */
    RenderMaterial toAssociated(RenderMaterial material);

    /**
     * Wraps a quad-sink that is potentially from another backend in one compatible with this backend.
     *
     * @param quadSink the quad-sink to wrap.
     * @return a new wrapper quad-sink that is compatible with this backend.
     */
    QuadSink toAssociated(QuadSink quadSink);
}
