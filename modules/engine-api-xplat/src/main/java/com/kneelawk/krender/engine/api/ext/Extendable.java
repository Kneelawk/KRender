package com.kneelawk.krender.engine.api.ext;

import org.jetbrains.annotations.Nullable;

/**
 * Describes a type that has optional extensions that are only provided by some backends.
 *
 * @param <S> the type that can be extended.
 */
public interface Extendable<S> {
    /**
     * Gets an extended version of this extendable if present.
     *
     * @param type the type of extension to get.
     * @param <T>  the type of extension to get.
     * @return the requested extension or {@code null} if this extendable does not provide the requested extension.
     */
    @SuppressWarnings("unchecked")
    default <T extends S> @Nullable T getExtension(Class<T> type) {
        if (type.isAssignableFrom(getClass())) return (T) this;
        return null;
    }

    /**
     * Gets an extended version of this extendable or throws if it is not present.
     *
     * @param type the type of extension to get.
     * @param <T>  the type of extension to get.
     * @return the requested extension.
     */
    default <T extends S> T getExtensionOrThrow(Class<T> type) {
        T extension = getExtension(type);
        if (extension == null) {
            throw new UnsupportedOperationException(
                "The extension '" + type.getName() + "' is not supported by '" + getClass().getName() + "'");
        }
        return extension;
    }
}
