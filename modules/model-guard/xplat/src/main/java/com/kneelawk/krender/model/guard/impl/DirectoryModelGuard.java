package com.kneelawk.krender.model.guard.impl;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import com.kneelawk.krender.model.guard.api.ModelGuard;

public final class DirectoryModelGuard implements ModelGuard {
    public static final MapCodec<DirectoryModelGuard> MAP_CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            Identifier.CODEC.fieldOf("loader").forGetter(DirectoryModelGuard::loader),
            Codec.STRING.fieldOf("directory").forGetter(DirectoryModelGuard::directory),
            Codec.STRING.optionalFieldOf("prefix", "").forGetter(DirectoryModelGuard::prefix)
        ).apply(instance, DirectoryModelGuard::new));

    private final Identifier loader;
    private final @Nullable String namespace;
    private final String directory;
    private final String prefix;

    public DirectoryModelGuard(Identifier loader, String directory, String prefix) {
        this.loader = loader;
        if (directory.contains(":")) {
            Identifier location = Identifier.parse(directory);
            this.namespace = location.getNamespace();
            this.directory = location.getPath();
        } else {
            this.namespace = null;
            this.directory = directory;
        }
        this.prefix = prefix;
    }

    @Override
    public MapCodec<? extends ModelGuard> getCodec() {
        return MAP_CODEC;
    }

    @Override
    public Identifier getLoader() {
        return loader;
    }

    @Override
    public Map<Identifier, Resource> loadAll(ResourceManager manager, String suffix) {
        FileToIdConverter converter = new FileToIdConverter(directory, suffix);
        Stream<Map.Entry<Identifier, Resource>> stream =
            converter.listMatchingResources(manager).entrySet().stream();
        if (namespace != null) {
            stream = stream.filter(e -> e.getKey().getNamespace().equals(namespace));
        }
        return stream
            .map(e -> Pair.of(converter.fileToId(e.getKey()).withPrefix(prefix), e.getValue()))
            .collect(Object2ObjectOpenHashMap::new, (map, pair) -> map.put(pair.key(), pair.value()),
                Object2ObjectOpenHashMap::putAll);
    }

    @Override
    public Optional<Resource> load(ResourceManager manager, Identifier id) {
        if (!id.getPath().startsWith(prefix)) return Optional.empty();
        String path = id.getPath().substring(prefix.length());

        if (namespace != null && !namespace.equals(id.getNamespace())) return Optional.empty();

        return manager.getResource(id.withPath(directory + "/" + path));
    }

    private Identifier loader() {return loader;}

    private String directory() {
        if (namespace == null) {
            return directory;
        } else {
            return namespace + ":" + directory;
        }
    }

    private String prefix() {return prefix;}
}
