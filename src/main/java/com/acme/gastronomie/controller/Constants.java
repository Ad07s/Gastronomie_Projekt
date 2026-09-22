package com.acme.gastronomie.controller;

/**
 * Zentrale Konstanten für die REST-Schnittstelle.
 */
public final class Constants {
    // Der Basis-Pfad deiner API
    public static final String API_PATH = "/api/restaurants";

    // Regex-Pattern für eine UUID (wichtig für die Pfad-Validierung)
    public static final String ID_PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-" +
            "[0-9a-fA-F]{12}";

    // API Versionierung
    public static final String VERSION_1 = "1";
    public static final String VERSION_1_EXAMPLE = "1";
    public static final String X_VERSION = "X-Version";

    private Constants() {
        // Privater Konstruktor, damit niemand die Klasse instanziiert
    }
}
