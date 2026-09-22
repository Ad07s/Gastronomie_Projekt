package com.acme.gastronomie.config;

import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;

/// Migrationsstrategie für _Flyway_ im Profile `dev`: Tabellen, Indexe etc. löschen und dann neu aufbauen.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
sealed interface Flyway permits DevConfig {
    /// Bean-Definition, um eine Migrationsstrategie für _Flyway_ im Profile `dev` bereitzustellen, so dass zuerst alle
    /// Tabellen, Indexe etc. gelöscht und dann neu aufgebaut werden.
    ///
    /// @return `FlywayMigrationStrategy`
    @Bean
    default FlywayMigrationStrategy flywayMigrationStrategy() {
        // https://www.javadoc.io/doc/org.flywaydb/flyway-core/latest/org/flywaydb/core/Flyway.html
        return flyway -> {
            // Loeschen aller DB-Objekte im Schema: Tabellen, Indexe, Stored Procedures, Trigger, Views, ...
            // insbesondere die Tabelle flyway_schema_history
            flyway.clean();
            // Start der DB-Migration
            flyway.migrate();
        };
    }
}
