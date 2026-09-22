package com.acme.gastronomie.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AdresseDTO(
        @NotNull
        @Pattern(regexp = PLZ_PATTERN, message = "PLZ muss 5-stellig sein")
        String plz,

        @NotBlank(message = "Ort darf nicht leer sein")
        String ort,

        @NotBlank(message = "Strasse darf nicht leer sein")
        String strasse


) {

    static final String PLZ_PATTERN = "^\\d{5}$";
}
