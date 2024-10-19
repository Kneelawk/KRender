package com.kneelawk.krender.model.guard.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ReferenceLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

/**
 * A set of all model guards. Useful for allowing model loaders to know which models they are allowed to load.
 */
public class ModelGuards {
    static final Map<ResourceLocation, MapCodec<? extends ModelGuard>> GUARD_CODECS =
        Collections.synchronizedMap(new Object2ReferenceLinkedOpenHashMap<>());
    static final Map<MapCodec<? extends ModelGuard>, ResourceLocation> REVERSE_CODECS =
        Collections.synchronizedMap(new Reference2ObjectLinkedOpenHashMap<>());
    static final Codec<MapCodec<? extends ModelGuard>> LOOKUP = ResourceLocation.CODEC.flatXmap(rl -> {
        var codec = GUARD_CODECS.get(rl);
        if (codec == null) return DataResult.error(() -> "No model guard with type '" + rl + "'");
        return DataResult.success(codec);
    }, codec -> {
        ResourceLocation rl = REVERSE_CODECS.get(codec);
        if (rl == null) return DataResult.error(() -> "Codec '" + codec + "' was never registered");
        return DataResult.success(rl);
    });

    /**
     * Registers a guard codec.
     *
     * @param type  the type of the guard.
     * @param codec the guard codec.
     */
    public static void registerGuardCodec(ResourceLocation type, MapCodec<? extends ModelGuard> codec) {
        if (GUARD_CODECS.putIfAbsent(type, codec) != null)
            throw new IllegalArgumentException("Guard '" + type + "' has already been registered");
        REVERSE_CODECS.putIfAbsent(codec, type);
    }

    /**
     * The location where guards are stored in each resource pack.
     */
    public static final String GUARDS_PATH = "krender/model_guards";

    private final Map<ResourceLocation, List<ModelGuard>> guards;

    private ModelGuards(Map<ResourceLocation, List<ModelGuard>> guards) {this.guards = guards;}

    /**
     * Actually loads the model resources that all guards have permitted to be loaded by the given loader.
     *
     * @param manager    the resource manager to load resources from.
     * @param loaderName the name of the model loader to load resources for.
     * @return the model resources for the given loader.
     */
    public Map<ResourceLocation, Resource> getModels(ResourceManager manager, ResourceLocation loaderName) {

    }
}
