package com.acme.util;

import java.util.function.Supplier;
/// Hilfsklasse für verzögerte Initialisierung (Stil des Professors).
public final class LazyConstant<T> {
    private final Supplier<T> supplier;
    private T value;

    private LazyConstant(final Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> LazyConstant<T> of(final Supplier<T> supplier) {
        return new LazyConstant<>(supplier);
    }

    public T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }
}
