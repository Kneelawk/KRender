package com.kneelawk.krender.model.creation.api.data;

/**
 * Key for a {@link DataHolder}.
 *
 * @param <T> the type of value this key is associated with.
 */
public class DataKey<T> {
    private DataKey() {}

    /**
     * Creates a new {@link DataKey} for the given type.
     *
     * @param <T> the type of value the new key will be associated with.
     * @return the new data-key.
     */
    public static <T> DataKey<T> create() {
        return new DataKey<>();
    }
}
