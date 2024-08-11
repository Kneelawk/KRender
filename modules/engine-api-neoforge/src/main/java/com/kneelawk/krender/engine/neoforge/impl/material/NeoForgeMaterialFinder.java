package com.kneelawk.krender.engine.neoforge.impl.material;

import com.kneelawk.krender.engine.api.TriState;
import com.kneelawk.krender.engine.api.material.BlendMode;
import com.kneelawk.krender.engine.api.material.MaterialFinder;
import com.kneelawk.krender.engine.api.material.MaterialView;
import com.kneelawk.krender.engine.api.material.RenderMaterial;

public class NeoForgeMaterialFinder implements MaterialFinder {
    private static final ThreadLocal<NeoForgeMaterialFinder> FINDER_POOL =
        ThreadLocal.withInitial(NeoForgeMaterialFinder::new);

    public static NeoForgeMaterialFinder getOrCreate() {
        return FINDER_POOL.get().clear();
    }

    private BlendMode blendMode;
    private boolean colorIndexDisabled;
    private boolean emissive;
    private boolean diffuseDisabled;
    private TriState ambientOcclusion;

    @Override
    public RenderMaterial find() {
        // TODO: optimize this
        return new NeoForgeRenderMaterial(blendMode, colorIndexDisabled, emissive, diffuseDisabled, ambientOcclusion);
    }

    @Override
    public NeoForgeMaterialFinder clear() {
        blendMode = BlendMode.DEFAULT;
        colorIndexDisabled = false;
        emissive = false;
        diffuseDisabled = false;
        ambientOcclusion = TriState.DEFAULT;

        return this;
    }

    @Override
    public NeoForgeMaterialFinder setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
        return this;
    }

    @Override
    public NeoForgeMaterialFinder setColorIndexDisabled(boolean disabled) {
        this.colorIndexDisabled = disabled;
        return this;
    }

    @Override
    public NeoForgeMaterialFinder setEmissive(boolean emissive) {
        this.emissive = emissive;
        return this;
    }

    @Override
    public NeoForgeMaterialFinder setDiffuseDisabled(boolean disabled) {
        this.diffuseDisabled = disabled;
        return this;
    }

    @Override
    public NeoForgeMaterialFinder setAmbientOcclusionMode(TriState mode) {
        this.ambientOcclusion = mode;
        return this;
    }

    @Override
    public NeoForgeMaterialFinder copyFrom(MaterialView material) {
        setBlendMode(material.getBlendMode()).setColorIndexDisabled(material.isColorIndexDisabled())
            .setEmissive(material.isEmissive()).setDiffuseDisabled(material.isDiffuseDisabled())
            .setAmbientOcclusionMode(material.getAmbientOcclusionMode());
        return this;
    }

    @Override
    public BlendMode getBlendMode() {
        return blendMode;
    }

    @Override
    public boolean isColorIndexDisabled() {
        return colorIndexDisabled;
    }

    @Override
    public boolean isEmissive() {
        return emissive;
    }

    @Override
    public boolean isDiffuseDisabled() {
        return diffuseDisabled;
    }

    @Override
    public TriState getAmbientOcclusionMode() {
        return ambientOcclusion;
    }
}
