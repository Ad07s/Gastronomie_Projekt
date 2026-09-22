package com.acme.gastronomie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UuidGenerator;
import org.jspecify.annotations.Nullable;

import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Entity
@Table(schema = "gastronomie", name = "adresse")
public class Adresse {

    @Id
    @Nullable

    @UuidGenerator(style = VERSION_7)
    private UUID id;

    // Beinhaltet Straße und Hausnummer (z.B. "Kaiserstraße 1")
    @Column(nullable = false)
    private String strasse;

    @Column(nullable = false)
    private String plz;

    @Column(nullable = false)
    private String ort;

    // Standard-Konstruktor für Hibernate (Pflicht!)
    public Adresse() {
    }

    // All-Args-Konstruktor für deine Services und Testdaten
    public Adresse(@Nullable final UUID id, final String strasse, final String plz, final String ort) {
        this.id = id;
        this.strasse = strasse;
        this.plz = plz;
        this.ort = ort;
    }

    // Equals und HashCode basierend auf der ID – genau wie beim Professor
    @Override
    public final boolean equals(final Object other) {
        return other instanceof Adresse adr && Objects.equals(id, adr.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(final String strasse) {
        this.strasse = strasse;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(final String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(final String ort) {
        this.ort = ort;
    }

    @Override
    public String toString() {
        return "Adresse{" + "id=" + id + ", strasse='" + strasse + '\'' +
                ", plz='" + plz + '\'' + ", ort='" + ort + '\'' + '}';
    }
}
