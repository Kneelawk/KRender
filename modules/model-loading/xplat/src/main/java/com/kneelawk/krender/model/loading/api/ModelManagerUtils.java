package com.kneelawk.krender.model.loading.api;

import java.util.Set;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.resources.Identifier;

import com.kneelawk.krender.model.loading.impl.mixin.api.Duck_ModelManager;

/**
 * Extra {@link net.minecraft.client.resources.model.ModelManager} utilities and additions.
 */
public final class ModelManagerUtils {
    private ModelManagerUtils() {}

    /**
     * Gets an extra model registered via a {@link ModelManagerPlugin}.
     *
     * @param manager the model manager to get the extra model from.
     * @param name    the name of the extra model.
     * @return the extra model loaded and baked.
     * @see ModelManagerPlugin.Context#addExtraModel(Identifier)
     * @see ModelManagerPlugin.Context#addExtraModels(Set)
     */
    public static QuadCollection getExtraModel(ModelManager manager, Identifier name) {
        return ((Duck_ModelManager) manager).krender$getExtraModel(name);
    }
}
