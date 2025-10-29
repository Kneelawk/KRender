package com.kneelawk.krender.engine.api.data;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import org.jetbrains.annotations.Nullable;

/**
 * Immutable map of {@link DataKey}s to values.
 */
public class DataHolder {
    private static final DataHolder EMPTY = new DataHolder(ImmutableMap.of());

    private final Map<DataKey<?>, ?> data;
    private @Nullable Set<DataKey<?>> keySet;

    private DataHolder(Map<DataKey<?>, ?> data) {this.data = data;}

    /**
     * {@return all the keys in this holder}
     */
    public Set<DataKey<?>> keySet() {
        var current = keySet;
        if (current == null) {
            current = data.keySet();
            keySet = current;
        }
        return current;
    }

    /**
     * Checks if this data holder contains data with the given key.
     *
     * @param key the key to check.
     * @return {@code true} if this holder contains the given key, {@code false} otherwise.
     */
    public boolean containsKey(DataKey<?> key) {
        return data.containsKey(key);
    }

    /**
     * Gets the value associated with the given key, if it exists.
     *
     * @param key the key to get the value associatd with.
     * @param <T> the type of the value associated with the key.
     * @return the value associated with the given key, or {@code null} if the key is not associated with any value.
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(DataKey<T> key) {
        return (T) data.get(key);
    }

    /**
     * Creates a derivative data holder with the given key and value pair added.
     *
     * @param key   the new key.
     * @param value the value associated with the new key.
     * @param <T>   the type of value associated with the new key.
     * @return the derivative data holder.
     */
    public <T> DataHolder with(DataKey<T> key, T value) {
        return new DataHolder(ImmutableMap.<DataKey<?>, Object>builder().putAll(data).put(key, value).build());
    }

    /**
     * Creates a builder used for creating derivative data holders based on this one.
     *
     * @return the new derivative builder.
     */
    public Builder toBuilder() {
        return new Builder().putAll(this);
    }

    /**
     * {@return the empty data holder}
     */
    public static DataHolder empty() {
        return EMPTY;
    }

    /**
     * Creates a new {@link DataHolder}.
     *
     * @param key   the first key.
     * @param value the value associated with the first key.
     * @param <T>   the type of value associated with the first key.
     * @return the new data holder.
     */
    public static <T> DataHolder create(DataKey<T> key, T value) {
        return new DataHolder(ImmutableMap.of(key, value));
    }

    /**
     * Creates a new data holder builder without any data in it.
     *
     * @return the new data holder builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to create new {@link DataHolder}s in an ergonomic fashion.
     */
    public static class Builder {
        ImmutableMap.Builder<DataKey<?>, Object> builder = ImmutableMap.builder();

        private Builder() {
        }

        /**
         * Creates a new data holder with all the data contained in this builder.
         *
         * @return the new data holder.
         */
        public DataHolder build() {
            return new DataHolder(builder.build());
        }

        /**
         * Adds the given key-value pair to this builder.
         *
         * @param key   the key to add.
         * @param value the value associated with the key.
         * @param <T>   the type of the value associated with the key.
         * @return this builder for chaining.
         */
        public <T> Builder put(DataKey<T> key, T value) {
            builder.put(key, value);
            return this;
        }

        /**
         * Adds all the key-value pairs from the given holder to this holder builder.
         *
         * @param holder the existing holder to add to this one.
         * @return this builder for chaining.
         */
        public Builder putAll(DataHolder holder) {
            builder.putAll(holder.data);
            return this;
        }
    }
}
