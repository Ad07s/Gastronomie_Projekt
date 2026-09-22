package com.acme.gastronomie.repository;

import com.acme.gastronomie.entity.Restaurant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    /// Lädt ein Restaurant mit allen LAZY-Beziehungen (Adresse & Speisekarte)
    /// in EINEM Rutsch (Verhindert den Jackson-500-Fehler).
    @Query("""
           SELECT r 
           FROM   Restaurant r 
           LEFT JOIN FETCH r.adresse 
           LEFT JOIN FETCH r.speisekarte 
           WHERE  r.id = :id
           """)
    Optional<Restaurant> findByIdWithDetails(@Param("id") UUID id);

    /// Lädt alle Restaurants inklusive aller Details mittels Fetch-Join.
    @Query("""
           SELECT DISTINCT r 
           FROM   Restaurant r 
           LEFT JOIN FETCH r.adresse 
           LEFT JOIN FETCH r.speisekarte
           """)
    List<Restaurant> findAllWithDetails();

    /// Sucht Restaurants nach Namensanteilen (Case-Insensitive) inklusive Details.
    @Query("""
           SELECT r 
           FROM   Restaurant r 
           LEFT JOIN FETCH r.adresse 
           LEFT JOIN FETCH r.speisekarte 
           WHERE  LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))
           """)
    List<Restaurant> findByNameContainingWithDetails(@Param("name") String name);

    /// Spring Data JPA generiert die Existenzprüfung anhand des Methodennamens automatisch.
    boolean existsByNameIgnoreCase(String name);


    // =========================================================================
    //  ABWÄRTSKOMPATIBILITÄT FÜR MEINEN SERVICE & CONTROLLER
    // =========================================================================
// =========================================================================
    //  ABWÄRTSKOMPATIBILITÄT FÜR MEINEN SERVICE & CONTROLLER
    // =========================================================================

    /// Liefert das Restaurant direkt oder null (umbenannt, um Konflikt mit JPA zu vermeiden)
    default Restaurant findOneById(final UUID id) {
        return findByIdWithDetails(id).orElse(null);
    }

    default boolean isNameExisting(final String name) {
        return existsByNameIgnoreCase(name);
    }

    default Restaurant create(final Restaurant restaurant) {
        return save(restaurant);
    }

    default void update(final Restaurant restaurant) {
        save(restaurant);
    }

    default Collection<Restaurant> find(final Map<String, ? extends List<String>> parameter) {
        if (parameter.isEmpty()) {
            return findAllWithDetails();
        }
        if (parameter.containsKey("name")) {
            final String nameParam = parameter.get("name").getFirst();
            return findByNameContainingWithDetails(nameParam);
        }
        return findAllWithDetails();
    }
}
