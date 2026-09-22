package com.acme.gastronomie.service;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

/// [RuntimeException], falls kein Restaurant gefunden wurde.
public final class NotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;


    @Nullable
    private final UUID id;


    @Nullable
    private final Map<String, List<String>> suchparameter;


    NotFoundException() {
        super("Keine Restaurants gefunden.");
        id = null;
        suchparameter = null;
    }


    NotFoundException(final UUID id) {
        super("Kein Restaurant mit der ID " + id + " gefunden.");
        this.id = id;
        suchparameter = null;
    }


    NotFoundException(final Map<String, List<String>> suchparameter) {
        super("Keine Restaurants mit den angegebenen Parametern gefunden.");
        id = null;
        this.suchparameter = suchparameter;
    }

    public @Nullable UUID getId() {
        return id;
    }

    public @Nullable Map<String, List<String>> getSuchparameter() {
        return suchparameter;
    }
}
