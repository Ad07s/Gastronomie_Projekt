package com.acme.gastronomie.service;

import com.acme.gastronomie.repository.AdresseBuilder;
import com.acme.gastronomie.repository.RestaurantBuilder;
import com.acme.gastronomie.repository.RestaurantRepository;
import com.acme.gastronomie.repository.SpeiseBuilder;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.gaul.modernizer_maven_annotations.SuppressModernizer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.condition.JRE.JAVA_26;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Tag("unit")
@Tag("service-write")
@DisplayName("Geschaeftslogik fuer Schreiben testen")
@Execution(CONCURRENT)
@EnabledForJreRange(min = JAVA_26, max = JAVA_26)
@ExtendWith(SoftAssertionsExtension.class)
@SuppressWarnings("WriteTag")
class RestaurantWriteServiceTest {
    private static final String NEUE_PLZ = "76133";
    private static final String NEUER_ORT = "Karlsruhe";
    private static final String NEUER_NAME = "Burgerheart";
    private static final String ID_UPDATE = "00000000-0000-0000-0000-000000000001";
    private static final String ID_DELETE = "00000000-0000-0000-0000-000000000002";

    private final RestaurantWriteService service;
    private final RestaurantRepository repo;

    @InjectSoftAssertions
    @SuppressWarnings("NullAway.Init")
    private SoftAssertions softly;

    @SuppressWarnings("PMD.AvoidAccessibilityAlteration")
    @SuppressFBWarnings("CT_CONSTRUCTOR_THROW")
    RestaurantWriteServiceTest() {
        final var constructor = RestaurantRepository.class.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        try {
            repo = (RestaurantRepository) constructor.newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalStateException(ex);
        }
        service = new RestaurantWriteService(repo);
    }

    @ParameterizedTest(name = "[{index}] Neuanlegen: name={0}, plz={1}")
    @CsvSource(NEUER_NAME + "," + NEUE_PLZ + "," + NEUER_ORT)
    @DisplayName("Neuanlegen eines neuen Restaurants")
    void create(final ArgumentsAccessor args) {
        final var name = args.getString(0);
        final var plz = args.getString(1);
        final var ort = args.getString(2);

        final var adresse = AdresseBuilder.getBuilder()
                .setPlz(plz).setOrt(ort).build();
        final var speisen = List.of(
                SpeiseBuilder.getBuilder().setName("Pizza").setPreis(new BigDecimal(10)).build()
        );
        final var restaurant = RestaurantBuilder.getBuilder()
                .setName(name).setAdresse(adresse).setSpeisekarte(speisen).build();

        final var restaurantCreated = service.create(restaurant);

        softly.assertThat(restaurantCreated.getId()).isNotNull();
        softly.assertThat(restaurantCreated.getName()).isEqualTo(NEUER_NAME);
        softly.assertThat(restaurantCreated.getAdresse().getPlz()).isEqualTo(NEUE_PLZ);
    }

    @ParameterizedTest(name = "[{index}] Aendern: id={0}")
    @ValueSource(strings = ID_UPDATE)
    @SuppressModernizer
    void update(final String id) {
        final var restId = UUID.fromString(id);

        // 1. HIER ÄNDERN: findOneById statt findById verwenden
        final var restaurant = repo.findOneById(restId);
        assertThat(restaurant).isNotNull();
        restaurant.setName(NEUER_NAME);

        service.update(restaurant, restId);

        // 2. HIER ÄNDERN: findOneById statt findById verwenden
        final var result = repo.findOneById(restId);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(NEUER_NAME);
    }

    @ParameterizedTest(name = "[{index}] Loeschen: id={0}")
    @ValueSource(strings = ID_DELETE)
    void deleteById(final String id) {
        final var restId = UUID.fromString(id);
        service.deleteById(restId);

        // 3. HIER ÄNDERN: findOneById statt findById verwenden
        final var result = repo.findOneById(restId);
        assertThat(result).isNull();
    }
}
