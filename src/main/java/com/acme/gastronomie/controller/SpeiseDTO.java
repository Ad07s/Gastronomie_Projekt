package com.acme.gastronomie.controller;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SpeiseDTO(
        @NotBlank(message = "Name der Speise ist Pflicht")
        String name,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false, message = "Preis muss größer als 0 sein")
        BigDecimal preis
) {
}
