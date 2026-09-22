package com.acme.gastronomie.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.MultiValueMap;
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

@Tag("integration")
@Tag("rest")
@DisplayName("REST-Schnittstelle für Gastronomie testen")
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(DEV)
@EnabledForJreRange(min = JAVA_26, max = JAVA_26)
class RestaurantControllerTest {
    // IDs aus deiner SimulatedDB
    private static final String ID_VORHANDEN = "00000000-0000-7000-9000-000000000000";
    private static final String NAME_PARAM = "name";
    private final RestaurantRepository restaurantRepo;

    @InjectSoftAssertions
    private SoftAssertions softly;
    @SuppressFBWarnings("CT")
    RestaurantControllerTest(@LocalServerPort final int port, final ApplicationContext ctx) {
        assertThat(ctx).isNotNull();

        final var uriComponents = UriComponentsBuilder.newInstance()
                .scheme(SCHEMA).host(HOST).port(port).path(API_PATH).build();

        final var restClient = RestClient.builder()
                .requestFactory(REQUEST_FACTORY)
                .apiVersionInserter(API_VERSION_INSERTER)
                .baseUrl(uriComponents.toUriString())
                .build();

        final var proxyFactory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
        restaurantRepo = proxyFactory.createClient(RestaurantRepository.class);
    }

    @Nested
    @DisplayName("Lesen")
    class Lesen {

        @Test
        @DisplayName("1. Test: Lesen mit ID")
        void getById() {
            // when
            final var response = restaurantRepo.getById(ID_VORHANDEN);

            // then
            assertThat(response.getBody()).isNotNull();
            softly.assertThat(response.getBody().getId().toString()).isEqualTo(ID_VORHANDEN);
            softly.assertThat(response.getBody().getName()).isNotBlank();
        }

        @ParameterizedTest(name = "[{index}] 2. Test: Lesen mit Suchparameter (Name={0})")
        @ValueSource(strings = {"Admin Restaurant", "L Osteria"})
        void getBySuchparameter(final String name) {
            // given
            final var suchparameter = MultiValueMap.fromSingleValue(Map.of(NAME_PARAM, name));

            // when
            final var ergebnisse = restaurantRepo.get(suchparameter);

            // then
            softly.assertThat(ergebnisse).isNotNull().isNotEmpty();
            ergebnisse.forEach(r -> softly.assertThat(r.getName()).containsIgnoringCase(name));
        }
    }

    @Nested
    @DisplayName("Schreiben")
    class Schreiben {

        @Test
        @DisplayName("3. Test: Neuanlegen")
        void post() {
            // given
            final var dto = new RestaurantDTO(
                    "Neu Eroeffnet",
                    new AdresseDTO("76133", "Karlsruhe", "Hauptstraße 12"),
                    List.of(new SpeiseDTO("Pizza Test", new BigDecimal("100")))
            );

            // when
            final var response = restaurantRepo.post(dto);

            // then
            assertThat(response).isNotNull();
            softly.assertThat(response.getStatusCode()).isEqualTo(CREATED);
            final URI location = response.getHeaders().getLocation();
            assertThat(location).isNotNull();
            assertThat(location.toString()).matches(".*/" + ID_PATTERN + "$");
        }
    }
}
