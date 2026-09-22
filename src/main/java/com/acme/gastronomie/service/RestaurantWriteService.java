package com.acme.gastronomie.service;

import com.acme.gastronomie.entity.Restaurant;
import com.acme.gastronomie.repository.RestaurantRepository;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RestaurantWriteService {
    private final RestaurantRepository repo;
    private final LazyConstant<Logger> logger = LazyConstant.of(() -> LoggerFactory
            .getLogger(RestaurantWriteService.class));

    RestaurantWriteService(final RestaurantRepository repo) {
        this.repo = repo;
    }

    public Restaurant create(final Restaurant restaurant) {
        logger.get().debug("create: {}", restaurant);


        if (repo.isNameExisting(restaurant.getName())) {
            throw new NameExistsException(restaurant.getName());
        }

        final var restaurantDB = repo.create(restaurant);
        logger.get().debug("create erfolgreich: {}", restaurantDB);
        return restaurantDB;
    }

    public void update(final Restaurant restaurant, final UUID id) {
        logger.get().debug("update: {}, id={}", restaurant, id);

        final var neuerName = restaurant.getName();
        
        final var restaurantDb = repo.findOneById(id);

        if (restaurantDb == null) {
            throw new NotFoundException(id);
        }

        if (!Objects.equals(neuerName, restaurantDb.getName()) && repo.isNameExisting(neuerName)) {
            logger.get().debug("update: Name {} existiert bereits", neuerName);
            throw new NameExistsException(neuerName);
        }

        restaurant.setId(id);
        repo.update(restaurant);
    }

    public void deleteById(UUID restId) {
    }
}
    