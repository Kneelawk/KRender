package com.kneelawk.krender.engine.backend.frapi.api;

import com.kneelawk.krender.engine.api.TriState;
import com.kneelawk.krender.engine.api.material.BlendMode;

/**
 * Utility class for converting between KRender types and FRAPI types.
 */
public final class ConversionUtils {
    private ConversionUtils() {}

    /**
     * Converts a FRAPI blend-mode to a KRender blend-mode.
     *
     * @param blendMode the FRAPI blend mode.
     * @return the equivalent KRender blend mode.
     */
    public static BlendMode toKRender(net.fabricmc.fabric.api.renderer.v1.material.BlendMode blendMode) {
        return switch (blendMode) {
            case DEFAULT -> BlendMode.DEFAULT;
            case SOLID -> BlendMode.SOLID;
            case CUTOUT_MIPPED -> BlendMode.CUTOUT_MIPPED;
            case CUTOUT -> BlendMode.CUTOUT;
            case TRANSLUCENT -> BlendMode.TRANSLUCENT;
        };
    }

    /**
     * Converts a KRender blend-mode to a FRAPI blend-mode.
     *
     * @param blendMode the KRender blend-mode.
     * @return the equivalent FRAPI blend-mode.
     */
    public static net.fabricmc.fabric.api.renderer.v1.material.BlendMode toFabric(BlendMode blendMode) {
        return switch (blendMode) {
            case DEFAULT -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.DEFAULT;
            case SOLID -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.SOLID;
            case CUTOUT_MIPPED -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.CUTOUT_MIPPED;
            case CUTOUT -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.CUTOUT;
            case TRANSLUCENT -> net.fabricmc.fabric.api.renderer.v1.material.BlendMode.TRANSLUCENT;
        };
    }

    /**
     * Converts a FAPI tri-state to a KRender tri-state.
     *
     * @param triState the FAPI tri-state.
     * @return the equivalent KRender tri-state.
     */
    public static TriState toKRender(net.fabricmc.fabric.api.util.TriState triState) {
        return switch (triState) {
            case FALSE -> TriState.FALSE;
            case DEFAULT -> TriState.DEFAULT;
            case TRUE -> TriState.TRUE;
        };
    }

    /**
     * Converts a KRender tri-state to a FAPI tri-state.
     *
     * @param triState the KRender tri-state.
     * @return the equivalent FAPI tri-state.
     */
    public static net.fabricmc.fabric.api.util.TriState toFabric(TriState triState) {
        return switch (triState) {
            case FALSE -> net.fabricmc.fabric.api.util.TriState.FALSE;
            case DEFAULT -> net.fabricmc.fabric.api.util.TriState.DEFAULT;
            case TRUE -> net.fabricmc.fabric.api.util.TriState.TRUE;
        };
    }
}
