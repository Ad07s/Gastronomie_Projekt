package com.acme.gastronomie.service;

import java.io.Serial;

/// Exception, falls der Restaurantname bereits existiert.
public class NameExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /// Bereits vorhandener Name.
    private final String name;

    /// Konstruktor für doppelte Namen.
    ///
    /// @param name Der bereits existierende Name.
    NameExistsException(@SuppressWarnings("ParameterHidesMemberVariable") final String name) {
        super("Das Restaurant mit dem Namen " + name + " existiert bereits.");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return super.getMessage() == null ? "Der Restaurantname existiert bereits." : super.getMessage();
    }
}
