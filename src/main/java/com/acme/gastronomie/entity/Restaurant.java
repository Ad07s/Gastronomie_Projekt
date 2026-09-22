package com.acme.gastronomie.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.jspecify.annotations.Nullable;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Entity
@Table(schema = "gastronomie", name = "restaurant")
public class Restaurant {

    @Id
    @Nullable

    @UuidGenerator(style = VERSION_7)
    private UUID id;

    @Version
    private int version;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "adresse_id", unique = true, nullable = false)
    private Adresse adresse;

    @Nullable
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "idx")
    @JoinColumn(name = "restaurant_id")
    private List<Speise> speisekarte;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime erzeugt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime aktualisiert;

    // Standard-Konstruktor für Hibernate (Pflicht!)
    public Restaurant() {
    }

    // All-Args-Konstruktor für deine Services/Tests
    public Restaurant(@Nullable final UUID id, final String name, final Adresse adresse,
                      @Nullable final List<Speise> speisekarte) {
        this.id = id;
        this.name = name;
        this.adresse = adresse;
        this.speisekarte = speisekarte;
    }

    @Override
    public final boolean equals(final Object other) {
        return other instanceof Restaurant rest && Objects.equals(id, rest.id);
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

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(final Adresse adresse) {
        this.adresse = adresse;
    }

    @Nullable
    public List<Speise> getSpeisekarte() {
        return speisekarte;
    }

    public void setSpeisekarte(final List<Speise> speisekarte) {
        this.speisekarte = speisekarte;
    }

    public LocalDateTime getErzeugt() {
        return erzeugt;
    }

    public LocalDateTime getAktualisiert() {
        return aktualisiert;
    }

    @Override
    public String toString() {
        return "Restaurant{" + "id=" + id + ", version=" + version + ", name='" + name + '\'' + "}";
    }
}