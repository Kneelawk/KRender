package com.kneelawk.krender.engine.backend.frapi.impl;

import java.util.function.BooleanSupplier;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;

public class FRAPIPredicate implements BooleanSupplier {
    @Override
    public boolean getAsBoolean() {
        if (RendererAccess.INSTANCE.hasRenderer()) {
            return true;
        } else {
            KRBFRLog.LOG.warn("Fabric Render API is present, but no FRAPI implementations are present");
            return false;
        }
    }
}
