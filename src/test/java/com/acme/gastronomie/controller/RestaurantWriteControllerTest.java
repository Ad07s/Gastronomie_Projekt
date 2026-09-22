package com.acme.gastronomie.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;
import static com.acme.gastronomie.config.DevConfig.DEV;
import static com.acme.gastronomie.controller.Constants.API_PATH;
import static com.acme.gastronomie.controller.Constants.ID_PATTERN;
import static com.acme.gastronomie.controller.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.condition.JRE.JAVA_26;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag("integration")
@Tag("rest")
@Tag("rest-write")
@DisplayName("REST-Schnittstelle fuer Schreibzugriffe testen")
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(DEV)
@EnabledForJreRange(min = JAVA_26, max = JAVA_26)
@SuppressWarnings({"WriteTag", "PMD.AtLeastOneConstructor"})
class RestaurantWriteControllerTest {
    private static final String ID_UPDATE_PUT = "00000000-0000-0000-0000-000000000001";
    private static final String NEUER_NAME = "L'Osteria SWA";
    private static final String NEUE_PLZ = "76133";
    private static final String NEUER_ORT = "Karlsruhe";

    private final RestaurantRepository restaurantRepo;
    @InjectSoftAssertions
    private SoftAssertions softly;
    @SuppressFBWarnings("CT")
    RestaurantWriteControllerTest(@LocalServerPort final int port, final ApplicationContext ctx) {
        assertThat(ctx).isNotNull();
        final var writeController = ctx.getBean(RestaurantWriteController.class);
        assertThat(writeController).isNotNull();

        final var uriComponents = UriComponentsBuilder.newInstance()
                .scheme(SCHEMA)
                .host(HOST)
                .port(port)
                .path(API_PATH)
                .build();

        final var restClient = RestClient.builder()
                .requestFactory(REQUEST_FACTORY)
                .apiVersionInserter(API_VERSION_INSERTER)
                .baseUrl(uriComponents.toUriString())
                .build();

        final var proxyFactory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        restaurantRepo = proxyFactory.createClient(RestaurantRepository.class);
    }
    @Nested
    @DisplayName("Erzeugen")
    class Erzeugen {
        @ParameterizedTest(name = "[{index}] Neuanlegen eines Restaurants: name={0}, plz={1}")
        @CsvSource(NEUER_NAME + "," + NEUE_PLZ + "," + NEUER_ORT)
        @DisplayName("Neuanlegen eines Restaurants")
        void post(final ArgumentsAccessor args) {
            // given
            final var name = args.getString(0);
            final var plz = args.getString(1);
            final var ort = args.getString(2);

            final var restaurantDTO = new RestaurantDTO(
                    name,
                    new AdresseDTO(plz, ort, "Hauptstraße 12"),
                    List.of(new SpeiseDTO("Pizza Test", new BigDecimal("12.50")))
            );
            // when
            final var response = restaurantRepo.post(restaurantDTO);
            // then
            assertThat(response).isNotNull();
            softly.assertThat(response.getStatusCode()).isEqualTo(CREATED);
            final var location = response.getHeaders().getLocation();
            assertThat(location).isNotNull().isInstanceOf(URI.class);
            assertThat(location.toString()).matches(".*/" + ID_PATTERN + "$");
        }
    }
    @Nested
    @DisplayName("Aendern")
    class Aendern {
        @ParameterizedTest(name = "[{index}] Aendern durch PUT: id={0}")
        @ValueSource(strings = ID_UPDATE_PUT)
        @DisplayName("Aendern eines vorhandenen Restaurants")
        void put(final String id) {
            // given
            final var responseGet = restaurantRepo.getById(id);
            final var restaurantOrig = responseGet.getBody();
            assertThat(restaurantOrig).isNotNull();

            final var restaurantDTO = new RestaurantDTO(
                    restaurantOrig.getName() + " Updated",
                    new AdresseDTO(restaurantOrig.getAdresse().getPlz(), restaurantOrig.getAdresse().getOrt(),
                            restaurantOrig.getAdresse().getStrasse()),
                    List.of(new SpeiseDTO("Pizza Update", new BigDecimal("15.00")))
            );
            // when
            final var response = restaurantRepo.put(id, restaurantDTO);

            // then
            assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        }
    }
}