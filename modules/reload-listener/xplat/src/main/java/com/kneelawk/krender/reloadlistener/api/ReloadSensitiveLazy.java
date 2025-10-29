package com.kneelawk.krender.reloadlistener.api;

import java.util.function.Supplier;

import org.jetbrains.annotations.UnknownNullability;

import com.kneelawk.commonevents.api.Listen;

/**
 * Holds something until a reload happens.
 *
 * @param <T> the thing being held.
 */
public final class ReloadSensitiveLazy<T> implements Supplier<T> {
    private static final Object UNINITIALIZED = new Object();

    private Object held = UNINITIALIZED;
    private final Supplier<T> supplier;

    /**
     * Creates a new {@link ReloadSensitiveLazy}.
     *
     * @param supplier the supplier for the lazily-initialized value.
     */
    public ReloadSensitiveLazy(Supplier<T> supplier) {
        this.supplier = supplier;

        // weakly register this object to receive reload events
        ReloadListenerEvents.PRE_RELOAD.registerAllWeak(this);
    }

    /**
     * Drops the held value due to a reload. This is called internally.
     */
    @Listen(ReloadListenerEvents.Pre.class)
    public void onReload() {
        held = UNINITIALIZED;
    }

    /**
     * Lazily initializes and returns the held value.
     *
     * @return the held value, once initialized.
     */
    @SuppressWarnings("unchecked")
    @Override
    public @UnknownNullability T get() {
        if (held == UNINITIALIZED) {
            held = supplier.get();
        }
        return (T) held;
    }
}
