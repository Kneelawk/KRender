package com.kneelawk.krender.engine.backend.frapi.impl.material;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;

import net.minecraft.resources.ResourceLocation;

import com.kneelawk.krender.engine.api.material.MaterialFinder;
import com.kneelawk.krender.engine.api.material.MaterialManager;
import com.kneelawk.krender.engine.api.material.RenderMaterial;

public class FRAPIMaterialManager implements MaterialManager {
    public static final FRAPIMaterialManager INSTANCE = new FRAPIMaterialManager();
    private static final Renderer frapiRenderer = RendererAccess.INSTANCE.getRenderer();

    // we might as well make material-lookup thread-safe, because we already have the overhead of getOrCreate
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public MaterialFinder materialFinder() {
        return FRAPIMaterialFinder.getOrCreate();
    }

    @Override
    public RenderMaterial defaultMaterial() {
        return FRAPIRenderMaterial.DEFAULT;
    }

    @Override
    public RenderMaterial missingMaterial() {
        return FRAPIRenderMaterial.DEFAULT;
    }

    @Override
    public @Nullable RenderMaterial materialById(ResourceLocation id) {
        lock.readLock().lock();
        try {
            return FRAPIRenderMaterial.getOrCreate(frapiRenderer.materialById(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean registerMaterial(ResourceLocation id, RenderMaterial material) {
        lock.writeLock().lock();
        try {
            return frapiRenderer.registerMaterial(id, ((FRAPIRenderMaterial) material).material);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
