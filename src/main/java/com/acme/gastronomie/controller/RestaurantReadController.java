package com.acme.gastronomie.controller;

import com.acme.gastronomie.entity.Restaurant;
import com.acme.gastronomie.service.RestaurantService;
import com.acme.util.LazyConstant;
import java.util.Collection;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
class RestaurantReadController {
    private final RestaurantService service;
    private final RestaurantMapper mapper;

    private final LazyConstant<Logger> logger = LazyConstant.of(
            () -> LoggerFactory.getLogger(RestaurantReadController.class)
    );

    // Konstruktor-Injection für Service UND Mapper
    RestaurantReadController(final RestaurantService service, final RestaurantMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /// URL: GET https://localhost:8443/api/restaurants/{id}
    /// Unterstützt bedingte GET-Requests mittels ETag / If-None-Match.
    @GetMapping("/{id}")
    ResponseEntity<Object> getById(
            @PathVariable final UUID id,
            @RequestHeader("If-None-Match") @Nullable final String ifNoneMatch
    ) {
        logger.get().debug("getById: id={}, ifNoneMatch={}", id, ifNoneMatch);

        final var restaurant = service.findById(id);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        }

        // ETag aus der Versionsnummer generieren (in Anführungszeichen verpackt)
        final var versionStr = "\"" + restaurant.getVersion() + '"';

        // Bedingter GET-Request: Hat sich nichts geändert? -> 304 Not Modified
        if (versionStr.equals(ifNoneMatch)) {
            logger.get().trace("getById: Version stimmt überein -> 304 Not Modified");
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        logger.get().trace("getById: Sende Restaurant mit neuem ETag");

        // Entity in DTO umwandeln, um Jackson-500er-Fehler bei LAZY-Beziehungen zu verhindern
        final RestaurantDTO dto = mapper.DTO(restaurant);

        return ResponseEntity.ok().eTag(versionStr).body(dto);
    }

    /// URL: GET https://localhost:8443/api/restaurants
    @GetMapping
    Collection<RestaurantDTO> get(
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final Integer sterne) {

        logger.get().debug("get: name={}, sterne={}", name, sterne);

        // Auch die Liste wird komplett als DTO-Collection zurückgegeben
        return service.findDetailed(name, sterne).stream()
                .map(mapper::toDTO)
                .toList();
    }
}