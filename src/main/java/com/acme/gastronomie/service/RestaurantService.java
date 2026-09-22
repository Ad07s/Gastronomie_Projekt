package com.acme.gastronomie.service;

import com.acme.gastronomie.entity.Restaurant;
import com.acme.gastronomie.repository.RestaurantRepository;
import com.acme.util.LazyConstant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
    private final RestaurantRepository repo;
    private final LazyConstant<Logger> logger = LazyConstant.of(
            () -> LoggerFactory.getLogger(RestaurantService.class)
    );

    RestaurantService(final RestaurantRepository repo) {
        this.repo = repo;
    }

    public Collection<Restaurant> find(final Map<String, List<String>> parameter) {
        logger.get().debug("Suche mit Parametern: {}", parameter);

        // Wir holen alle Restaurants und filtern hier im Service,
        // um sicherzustellen, dass "contains" (Teilsuche) funktioniert.
        final var nameList = parameter.get("name");
        final var name = (nameList == null || nameList.isEmpty()) ? null : nameList.get(0);

        final var  restaurants = repo.findAll().stream()
                .filter(r -> name == null || r.getName()
                        .toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)))
                .toList();

        logger.get().debug("Gefunden Restaurants: {}", restaurants);
        return restaurants;
    }

    public Restaurant findById(final UUID id) {
        logger.get().debug("findById: id={}", id);

        // 🛠️ HIER GEÄNDERT: findOneById(id) statt findById(id) verwenden
        final var restaurant = repo.findOneById(id);

        // WICHTIG: Wenn nichts gefunden wird, MUSS eine Exception geworfen werden,
        // damit der Test "catchThrowableOfType" erfolgreich ist.
        if (restaurant == null) {
            throw new NotFoundException(id);
        }

        return restaurant;
    }

    public Collection<Restaurant> findDetailed(final String name, final Integer sterne) {
        final Map<String, List<String>> params = new HashMap<>();

        if (name != null) {
            params.put("name", List.of(name));
        }

        if (sterne != null) {
            params.put("sterne", List.of(String.valueOf(sterne)));
        }

        return find(params);
    }
}
