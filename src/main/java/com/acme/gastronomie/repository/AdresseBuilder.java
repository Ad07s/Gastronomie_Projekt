package com.acme.gastronomie.repository;

import com.acme.gastronomie.entity.Adresse;

public class AdresseBuilder {
    private String strasse;
    private int hausnummer;
    private String plz;
    private String ort;

    AdresseBuilder() {

    }

    public static AdresseBuilder getBuilder() {
        return new AdresseBuilder();
    }

    public AdresseBuilder setStrasse(final String strasse) {
        this.strasse = strasse;
        return this;
    }

    public AdresseBuilder setHausnummer(final int hausnummer) {
        this.hausnummer = hausnummer;
        return this;
    }

    public AdresseBuilder setPlz(final String plz) {
        this.plz = plz;
        return this;
    }

    public AdresseBuilder setOrt(final String ort) {
        this.ort = ort;
        return this;
    }

    public Adresse build() {
        // Wir übergeben null für die ID (wird von DB generiert)
        // und hängen die Hausnummer einfach an die Straße an
        return new Adresse(null, strasse + " " + hausnummer, plz, ort);
    }
}
