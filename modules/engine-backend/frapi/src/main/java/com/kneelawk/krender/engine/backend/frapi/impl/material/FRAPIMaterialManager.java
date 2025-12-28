package com.kneelawk.krender.engine.backend.frapi.impl.material;

import java.util.concurrent.locks.ReentrantLock;

import net.fabricmc.fabric.api.renderer.v1.Renderer;

import net.minecraft.resources.Identifier;

import com.kneelawk.krender.engine.api.material.RenderMaterial;
import com.kneelawk.krender.engine.backend.frapi.impl.FRAPIRenderer;

public class FRAPIMaterialManager extends BaseMaterialManager {
    private final ReentrantLock lock = new ReentrantLock();

    public FRAPIMaterialManager() {
        super(FRAPIRenderer.INSTNACE, FRAPIRenderMaterial::new);
    }

    @Override
    public boolean registerMaterial(Identifier id, RenderMaterial material) {
        boolean res = super.registerMaterial(id, material);
        if (res) {
            lock.lock();
            try {
                Renderer.get().registerMaterial(id, ((FRAPIRenderMaterial) material).material);
            } finally {
                lock.unlock();
            }
        }
        return res;
    }

    @Override
    public boolean registerOrUpdateMaterial(Identifier id, RenderMaterial material) {
        lock.lock();
        try {
            Renderer.get().registerMaterial(id, ((FRAPIRenderMaterial) material).material);
        } finally {
            lock.unlock();
        }
        return super.registerOrUpdateMaterial(id, material);
    }
}
