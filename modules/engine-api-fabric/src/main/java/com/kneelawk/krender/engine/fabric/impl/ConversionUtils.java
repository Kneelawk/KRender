package com.kneelawk.krender.engine.fabric.impl;

import com.kneelawk.krender.engine.api.TriState;
import com.kneelawk.krender.engine.api.material.BlendMode;

public class ConversionUtils {
    public static BlendMode toKRender(net.fabricmc.fabric.api.renderer.v1.material.BlendMode blendMode) {
        return switch (blendMode) {
            case DEFAULT -> BlendMode.DEFAULT;
            case SOLID -> BlendMode.SOLID;
            case CUTOUT_MIPPED -> BlendMode.CUTOUT_MIPPED;
            case CUTOUT -> BlendMode.CUTOUT;
            case TRANSLUCENT -> BlendMode.TRANSLUCENT;
        };
    }

    public static net.fabricmc.fabric.api.renderer.v1.material.BlendMode toFabric(BlendMode blendMode) {
        return switch (blendMode) {
            case DEFAULT -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.DEFAULT;
            case SOLID -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.SOLID;
            case CUTOUT_MIPPED -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.CUTOUT_MIPPED;
            case CUTOUT -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.CUTOUT;
            case TRANSLUCENT -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.TRANSLUCENT;
        };
    }

    public static TriState toKRender(net.fabricmc.fabric.api.util.TriState triState) {
        return switch (triState) {
            case FALSE -> TriState.FALSE;
            case DEFAULT -> TriState.DEFAULT;
            case TRUE -> TriState.TRUE;
        };
    }

    public static net.fabricmc.fabric.api.util.TriState toFabric(TriState triState) {
        return switch (triState) {
            case FALSE -> net.fabricmc.fabric.api.util.TriState.FALSE;
            case DEFAULT -> net.fabricmc.fabric.api.util.TriState.DEFAULT;
            case TRUE -> net.fabricmc.fabric.api.util.TriState.TRUE;
        };
    }
}
