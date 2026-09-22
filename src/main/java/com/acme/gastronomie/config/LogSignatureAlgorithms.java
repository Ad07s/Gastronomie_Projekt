package com.acme.gastronomie.config;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import static org.springframework.context.annotation.Bean.Bootstrap.BACKGROUND;

/// Beim ApplicationReadyEvent werden Informationen für die Entwickler/innen im Hinblick auf Security (-Algorithmen)
/// protokolliert. Da es viele Algorithmen gibt und die Ausgabe lang wird, wird diese Funktionalität nur mit dem
/// Profile logSignature und nicht allgemein verwendet.
///
/// @author [Jürgen Zimmermann](mailto:Juergen.Zimmermann@h-ka.de)
sealed interface LogSignatureAlgorithms permits DevConfig {
    /// Bean-Definition, um einen _Listener_ bereitzustellen, damit die im JDK vorhandenen Signature-Algorithmen
    /// aufgelistet werden.
    ///
    /// @return Listener für die Ausgabe der Signature-Algorithmen
    @Bean(bootstrap = BACKGROUND)
    @Profile("logSignature")
    @SuppressWarnings("LambdaBodyLength")
    default ApplicationListener<ApplicationReadyEvent> logSignatureAlgorithms() {
        final var logger = LoggerFactory.getLogger(LogSignatureAlgorithms.class);
        return _ -> Arrays
                .stream(Security.getProviders())
                .forEach(provider -> logSignatureAlgorithms(provider, logger));
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    private void logSignatureAlgorithms(final Provider provider, final Logger logger) {
        provider
                .getServices()
                .forEach(service -> {
                    if ("Signature".contentEquals(service.getType())) {
                        logger.debug("{}", service.getAlgorithm());
                    }
                });
    }
}
