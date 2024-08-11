package com.kneelawk.krender.engine.api.material;

/**
 * Manages materials, material lookups, material caching, and material finding.
 */
public interface MaterialManager {

    /**
     * This gets or creates a material finder for this backend.
     * <p>
     * This method may cache returned material-finders in thread-locals.
     *
     * @return the requested material finder.
     */
    MaterialFinder materialFinder();

    /**
     * {@return this backend's default material}
     */
    RenderMaterial defaultMaterial();

    /**
     * {@return this backend's material that indicates a missing material}
     */
    RenderMaterial missingMaterial();
}
