package com.acme.gastronomie.service;

import com.acme.gastronomie.entity.Restaurant;
import com.acme.gastronomie.repository.RestaurantRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.junit.jupiter.api.condition.JRE.JAVA_26;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Tag("unit")
@Tag("service-read")
@DisplayName("Geschaeftslogik fuer Lesen testen")
@Execution(CONCURRENT)
@EnabledForJreRange(min = JAVA_26, max = JAVA_26)
@ExtendWith(SoftAssertionsExtension.class)
@SuppressWarnings({"WriteTag", "PMD.AtLeastOneConstructor"})
class RestaurantServiceTest {
    private static final String ID_VORHANDEN = "00000000-0000-0000-0000-000000000001";
    private static final String ID_NICHT_VORHANDEN = "ffffffff-ffff-ffff-ffff-ffffffffffff";
    private static final String NAME = "Trattoria";

    private final RestaurantService service;

    @InjectSoftAssertions
    @SuppressWarnings("NullAway.Init")
    private SoftAssertions softly;

    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    RestaurantServiceTest() {
        final var constructor = RestaurantRepository.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        final RestaurantRepository repo;
        try {
            repo = (RestaurantRepository) constructor.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalStateException(ex);
        }
        service = new RestaurantService(repo);
    }
    @Test
    @DisplayName("Suche nach allen Restaurants")
    void findAll() {
        final var restaurants = service.find(Collections.emptyMap());
        assertThat(restaurants).isNotEmpty();
    }
    @ParameterizedTest(name = "[{index}] Suche mit vorhandenem Namen: name={0}")
    @ValueSource(strings = NAME)
    @DisplayName("Suche mit vorhandenem Namen")
    void findByName(final String name) {
        final var params = Map.of("name", List.of(name));
        final var restaurants = service.find(params);
        softly.assertThat(restaurants).isNotEmpty();
        restaurants.stream()
                .map(Restaurant::getName)
                .forEach(n -> softly.assertThat(n).containsIgnoringCase(name));
    }
    @Nested
    @DisplayName("Suche anhand der ID")
    class FindById {
        @ParameterizedTest(name = "[{index}] Suche mit vorhandener ID: id={0}")
        @ValueSource(strings = ID_VORHANDEN)
        void findById(final String id) {
            final var restId = UUID.fromString(id);
            final var restaurant = service.findById(restId);
            assertThat(restaurant)
                    .isNotNull()
                    .extracting(Restaurant::getId)
                    .isEqualTo(restId);
        }
        @ParameterizedTest(name = "[{index}] Suche mit nicht-vorhandener ID: id={0}")
        @ValueSource(strings = ID_NICHT_VORHANDEN)
        void findByIdNichtVorhanden(final String id) {
            final var restId = UUID.fromString(id);
            final var notFoundException = catchThrowableOfType(
                    NotFoundException.class,
                    () -> service.findById(restId)
            );
            assertThat(notFoundException)
                    .isNotNull()
                    .extracting(NotFoundException::getId)
                    .isEqualTo(restId);
        }
    }
}
