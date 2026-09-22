package com.acme.gastronomie.repository;

import com.acme.gastronomie.entity.Speise;
import java.math.BigDecimal;

public class SpeiseBuilder {
    private String name;
    private BigDecimal preis;
    private boolean vegetarisch;
    private int kalorien;
    SpeiseBuilder() {

    }

    public static SpeiseBuilder getBuilder() {
        return new SpeiseBuilder();
    }

    public SpeiseBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public SpeiseBuilder setPreis(final BigDecimal preis) {
        this.preis = preis;
        return this;
    }

    public SpeiseBuilder setVegetarisch(final boolean vegetarisch) {
        this.vegetarisch = vegetarisch;
        return this;
    }

    public SpeiseBuilder setKalorien(final int kalorien) {
        this.kalorien = kalorien;
        return this;
    }

    public Speise build() {
        // Wir übergeben null für die ID (wird von DB generiert)
        // und nur noch Name und Preis
        return new Speise(null, name, preis);
    }
}
