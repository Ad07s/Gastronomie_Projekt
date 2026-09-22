package com.acme.gastronomie.repository;

import com.acme.gastronomie.entity.Restaurant;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("checkstyle:MagicNumber")
public final class SimulatedDB {


    private static final List<Restaurant> RESTAURANTS;

    static {
        RESTAURANTS = new ArrayList<>(Stream.of(
                RestaurantBuilder.getBuilder()
                        .setId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                        .setName("Trattoria Toscana")
                        .setSterne(5)
                        .setAdresse(
                                AdresseBuilder.getBuilder()
                                        .setStrasse("Hauptstraße")
                                        .setHausnummer(12)
                                        .setPlz("76133")
                                        .setOrt("Karlsruhe")
                                        .build()
                        )
                        .setSpeisekarte(List.of(
                                SpeiseBuilder.getBuilder()
                                        .setName("Pizza Margherita")
                                        .setPreis(new BigDecimal("9.50"))
                                        .setKalorien(750).build(),
                                SpeiseBuilder.getBuilder()
                                        .setName("Lasagne")
                                        .setPreis(new BigDecimal("12.50"))
                                        .setKalorien(2500).build()
                        ))
                        .build(),

                RestaurantBuilder.getBuilder()
                        .setId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                        .setName("L'Osteria")
                        .setSterne(4)
                        .setAdresse(
                                AdresseBuilder.getBuilder()
                                        .setStrasse("Marktplatz")
                                        .setHausnummer(5)
                                        .setPlz("76131")
                                        .setOrt("Karlsruhe")
                                        .build()
                        )
                        .setSpeisekarte(List.of(
                                SpeiseBuilder.getBuilder()
                                        .setName("Cheeseburger")
                                        .setPreis(new BigDecimal("14.00"))
                                        .setKalorien(1000).build(),
                                SpeiseBuilder.getBuilder()
                                        .setName("Pommes")
                                        .setPreis(new BigDecimal("4.50"))
                                        .setKalorien(300).build()
                        ))
                        .build(),

                RestaurantBuilder.getBuilder()
                        .setId(UUID.fromString("00000000-0000-0000-0000-000000000003"))
                        .setName("Sushi World")
                        .setSterne(5)
                        .setAdresse(
                                AdresseBuilder.getBuilder()
                                        .setStrasse("Waldstraße")
                                        .setHausnummer(22)
                                        .setPlz("76135")
                                        .setOrt("Karlsruhe")
                                        .build()
                        )
                        .setSpeisekarte(List.of(
                                SpeiseBuilder.getBuilder()
                                        .setName("Maki Mix")
                                        .setPreis(new BigDecimal("18.00"))
                                        .setKalorien(200).build()
                        ))
                        .build()
        ).collect(Collectors.toList()));
    }


    private SimulatedDB() { }

    /// Getter für die Restaurants (wichtig für isNameExisting im Repo)
    public static List<Restaurant> getRestaurants() {
        return Collections.unmodifiableList(RESTAURANTS);
    }

    /// Ein neues Restaurant zur Liste hinzufügen
    public static Restaurant add(final Restaurant restaurant) {
        // Falls noch keine ID vorhanden ist (beim Anlegen), generieren wir eine
        if (restaurant.getId() == null) {
            restaurant.setId(UUID.randomUUID());
        }
        RESTAURANTS.add(restaurant);
        return restaurant;
    }

    /// Ein vorhandenes Restaurant in der Liste ersetzen
    public static void update(final Restaurant restaurant) {
        final var id = restaurant.getId();
        RESTAURANTS.replaceAll(r -> r.getId().equals(id) ? restaurant : r);
    }

    /// Ein Restaurant aus der Liste entfernen
    public static void deleteById(final UUID id) {
        RESTAURANTS.removeIf(r -> r.getId().equals(id));
    }
}
