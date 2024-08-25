package com.kneelawk.krender.engine.backend.neoforge.impl.material;

import com.kneelawk.krender.engine.api.base.BaseMaterialView;
import com.kneelawk.krender.engine.api.material.RenderMaterial;

public class NFRenderMaterial extends BaseMaterialView implements RenderMaterial {
    /**
     * Creates a new {@link BaseMaterialView} with the given bits.
     *
     * @param bits the bits representing this material.
     */
    public NFRenderMaterial(int bits) {
        super(bits);
    }
}
