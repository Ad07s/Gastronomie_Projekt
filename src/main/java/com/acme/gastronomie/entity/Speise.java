package com.acme.gastronomie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UuidGenerator;
import org.jspecify.annotations.Nullable;

import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Entity
@Table(schema = "gastronomie", name = "speise")
public class Speise {

    @Id
    @Nullable

    @UuidGenerator(style = VERSION_7)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal preis;

    // Standard-Konstruktor für Hibernate (Pflicht!)
    public Speise() {
    }

    // All-Args-Konstruktor für deine Services/Testdaten
    public Speise(@Nullable final UUID id, final String name, final BigDecimal preis) {
        this.id = id;
        this.name = name;
        this.preis = preis;
    }

    // Equals und HashCode basierend auf der ID – analog zu Restaurant und Adresse
    @Override
    public final boolean equals(final Object other) {
        return other instanceof Speise sp && Objects.equals(id, sp.id);
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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPreis() {
        return preis;
    }

    public void setPreis(final BigDecimal preis) {
        this.preis = preis;
    }

    @Override
    public String toString() {
        return "Speise{" + "id=" + id + ", name='" + name + '\'' + ", preis=" + preis + '}';
    }
}
