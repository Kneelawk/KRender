package com.kneelawk.krender.ctcomplicated.registry;

import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static com.kneelawk.krender.ctcomplicated.CTConstants.rl;

public class Registrar<T> {
    private final ResourceKey<T> key;
    private final Map<ResourceLocation, Supplier<? extends T>> stuff = new Object2ObjectLinkedOpenHashMap<>();

    public Registrar(ResourceKey<T> key) {this.key = key;}

    public <T2 extends T> Supplier<T2> register(String path, Supplier<T2> ctor) {
        ResourceLocation name = rl(path);
        if (stuff.containsKey(name)) throw new IllegalArgumentException("Tried to register " + name + " twice!");

        Supplier<T2> memoized = new Supplier<>() {
            @Nullable
            private T2 value;

            @Override
            public T2 get() {
                T2 value = this.value;
                if (value == null) {
                    this.value = value = ctor.get();
                }
                return value;
            }
        };

        stuff.put(name, memoized);

        return memoized;
    }

    public void registerAll(Registry<? super T> registry) {
        if (key != registry.key()) return;

        for (var entry : stuff.entrySet()) {
            Registry.register(registry, entry.getKey(), entry.getValue().get());
        }
    }
}
