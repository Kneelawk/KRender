package com.kneelawk.krender.model.obj.impl.format;

import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

public record MtlMaterial(String name, float[] diffuseColor, float dissolve, @Nullable Identifier diffuseTexture,
                          boolean emissive) {
    public MtlMaterial(String name) {
        this(name, new float[0], 1f, null, false);
    }

    public MtlMaterial withDiffuseColor(float[] diffuseColor) {
        return new MtlMaterial(name, diffuseColor, dissolve, diffuseTexture, emissive);
    }

    public MtlMaterial withDissolve(float dissolve) {
        return new MtlMaterial(name, diffuseColor, dissolve, diffuseTexture, emissive);
    }

    public MtlMaterial withDiffuseTexture(Identifier diffuseTexture) {
        return new MtlMaterial(name, diffuseColor, dissolve, diffuseTexture, emissive);
    }

    public MtlMaterial withEmissive(boolean emissive) {
        return new MtlMaterial(name, diffuseColor, dissolve, diffuseTexture, emissive);
    }
}
