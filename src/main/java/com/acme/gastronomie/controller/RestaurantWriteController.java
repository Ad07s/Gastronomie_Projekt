package com.acme.gastronomie.controller;

import com.acme.gastronomie.controller.RestaurantDTO.OnCreate;
import com.acme.gastronomie.service.NameExistsException;
import com.acme.gastronomie.service.RestaurantWriteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.groups.Default;
import java.net.URI;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import static com.acme.gastronomie.controller.Constants.API_PATH;
import static com.acme.gastronomie.controller.Constants.ID_PATTERN;
import static com.acme.gastronomie.controller.Constants.VERSION_1;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_CONTENT;
import static org.springframework.http.ResponseEntity.created;


@Controller
@Validated
@RequestMapping(API_PATH)
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
class RestaurantWriteController {
    private final RestaurantWriteService service;
    private final RestaurantMapper mapper;
    private final UriHelper uriHelper;
    private final LazyConstant<Logger> logger =
            LazyConstant.of(() -> LoggerFactory.getLogger(RestaurantWriteController.class));

    RestaurantWriteController(final RestaurantWriteService service,
                              final RestaurantMapper mapper,
                              final UriHelper uriHelper) {
        this.service = service;
        this.mapper = mapper;
        this.uriHelper = uriHelper;
    }

    @PostMapping(version = VERSION_1)

    ResponseEntity<Void> post(
            @RequestBody @Validated({Default.class, OnCreate.class}) final RestaurantDTO restaurantDTO,
            final HttpServletRequest request
    ) {
        logger.get().debug("post: {}", restaurantDTO);

        final var restaurantInput = mapper.toRestaurant(restaurantDTO);
        final var restaurant = service.create(restaurantInput);

        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = URI.create(baseUri + '/' + restaurant.getId());
        return created(location).build();
    }

    @PutMapping(path = "{id:" + ID_PATTERN + "}", version = VERSION_1)
    @ResponseStatus(NO_CONTENT)
    void put(
            @PathVariable final UUID id,
            @RequestBody @Validated final RestaurantDTO restaurantDTO,
            @RequestHeader(HttpHeaders.IF_MATCH) final String ifMatch //  1. Header abfangen!
    ) {
        logger.get().debug("put: id={}, {}, If-Match={}", id, restaurantDTO, ifMatch);

        final var restaurantInput = mapper.toRestaurant(restaurantDTO);

        // 2. ETag-Anführungszeichen säubern und als Integer parsen
        final var versionStr = ifMatch.replace("\"", "").trim();
        final var version = Integer.parseInt(versionStr);

        // 3. Die korrekte Version auf die Entity setzen, damit Hibernate glücklich ist
        restaurantInput.setVersion(version);

        service.update(restaurantInput, id);
    }

    @ExceptionHandler
    ErrorResponse onConstraintViolations(final MethodArgumentNotValidException ex) {
        logger.get().debug("onConstraintViolations: {}", ex.getMessage());
        final var detailMessages = ex.getDetailMessageArguments();
        final var detail = detailMessages.length == 0 || detailMessages[1] == null
                ? "Constraint Violation"
                : ((String) detailMessages[1]).replace(", and ", ", ");
        return ErrorResponse.create(ex, UNPROCESSABLE_CONTENT, detail);
    }

    @ExceptionHandler
    ErrorResponse onNameExists(final NameExistsException ex) {
        logger.get().debug("onNameExists: {}", ex.getMessage());
        return ErrorResponse.create(ex, UNPROCESSABLE_CONTENT, ex.getMessage());
    }

    @ExceptionHandler
    ErrorResponse onMessageNotReadable(final HttpMessageNotReadableException ex) {
        final var msg = ex.getMessage() == null ? "N/A" : ex.getMessage();
        logger.get().debug("onMessageNotReadable: {}", msg);
        return ErrorResponse.create(ex, BAD_REQUEST, msg);
    }
}
