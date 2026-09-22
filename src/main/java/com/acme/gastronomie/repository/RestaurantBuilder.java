package com.acme.gastronomie.repository;

import com.acme.gastronomie.entity.Adresse;
import com.acme.gastronomie.entity.Restaurant;
import com.acme.gastronomie.entity.Speise;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public class RestaurantBuilder {
    private @Nullable UUID id;
    private String name;
    private int sterne;
    private boolean hatAussenbereich;
    private Adresse adresse;
    private @Nullable List<Speise> speisekarte;
    RestaurantBuilder() {

    }

    public static RestaurantBuilder getBuilder() {
        return new RestaurantBuilder();
    }

    public RestaurantBuilder setId(@Nullable  final UUID id) {
        this.id = id;
        return this;
    }

    public RestaurantBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public RestaurantBuilder setSterne(final int sterne) {
        this.sterne = sterne;
        return this;
    }

    public RestaurantBuilder setHatAussenbereich(final boolean hatAussenbereich) {
        this.hatAussenbereich = hatAussenbereich;
        return this;
    }

    public RestaurantBuilder setAdresse(final Adresse adresse) {
        this.adresse = adresse;
        return this;
    }

    public RestaurantBuilder setSpeisekarte(@Nullable final List<Speise> speisekarte) {
        this.speisekarte = speisekarte;
        return this;
    }

    public Restaurant build() {
        // Nur noch die 4 Felder übergeben, die die Entity wirklich hat
        return new Restaurant(id, name, adresse, speisekarte);

    }
}
