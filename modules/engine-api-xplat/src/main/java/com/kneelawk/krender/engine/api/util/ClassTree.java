package com.kneelawk.krender.engine.api.util;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

/**
 * Allows you to associate values with classes and then look them up by those classes or their descendants.
 *
 * @param <T> the value type this class tree associates with different classes.
 */
public class ClassTree<T> {
    private final Map<Class<?>, Map<Class<?>, Set<T>>> quickLookup = new HashMap<>();
    private final Map<Class<?>, Set<Class<?>>> descendantLookup = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final ArrayDeque<Class<?>> classQueue = new ArrayDeque<>();
    private final Set<Class<?>> visited = new ObjectOpenHashSet<>();

    /**
     * Creates a new class tree.
     */
    public ClassTree() {}

    /**
     * Adds a key-value pair to this class tree that can be looked up by the key class or any of its descendants.
     *
     * @param key   the key class to associate the value with.
     * @param value the value to associate with the key class and its descendants.
     */
    public void add(Class<?> key, T value) {
        lock.writeLock().lock();
        try {
            initClass(key);

            // descendant-lookup will have this class too
            for (Class<?> descendant : descendantLookup.get(key)) {
                // should always have a non-null set for every value in descendant-lookup
                quickLookup.get(descendant).computeIfAbsent(key, k -> new ObjectLinkedOpenHashSet<>()).add(value);
            }
        } finally {
            lock.writeLock().lock();
        }
    }

    /**
     * Looks for all values associated with a class any of its ancestors.
     *
     * @param key the class to look up.
     * @return a map of the type's ancestor classes and their associated values.
     */
    public Map<Class<?>, Set<T>> get(Class<?> key) {
        lock.readLock().lock();
        try {
            if (quickLookup.containsKey(key)) {
                return Collections.unmodifiableMap(quickLookup.get(key));
            }
        } finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            initClass(key);
            // initClass means that quick-lookup will always contain the specified key
            return Collections.unmodifiableMap(quickLookup.get(key));
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void initClass(Class<?> key) {
        if (quickLookup.containsKey(key)) return;

        // use an array-based set impl
        Map<Class<?>, Set<T>> lookup = new Object2ObjectLinkedOpenHashMap<>();

        // re-use class queue to save on allocation costs
        classQueue.clear();
        classQueue.add(key);

        // re-use visited set
        visited.clear();

        while (!classQueue.isEmpty()) {
            Class<?> current = classQueue.removeFirst();

            // only bother to visit a parent once
            if (!visited.add(current)) continue;

            // queue parent classes
            Class<?> superclass = current.getSuperclass();
            if (superclass != null) classQueue.add(superclass);
            Collections.addAll(classQueue, current.getInterfaces());

            // add all registrations of parents
            if (quickLookup.containsKey(current)) {
                for (Map.Entry<Class<?>, Set<T>> entry : quickLookup.get(current).entrySet()) {
                    lookup.computeIfAbsent(entry.getKey(), k -> new ObjectLinkedOpenHashSet<>())
                        .addAll(entry.getValue());
                }
            }

            // add key as a descendant
            descendantLookup.computeIfAbsent(current, c -> new ObjectLinkedOpenHashSet<>()).add(key);
        }

        // no need for these to stick around
        visited.clear();

        // actually add all collected objs to this quick lookup
        quickLookup.put(key, lookup);
    }
}
