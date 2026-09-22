package com.acme.gastronomie.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RestaurantDTO(
        @NotBlank(message = "Restaurantname darf nicht leer sein")
        String name,

        @Valid
        @NotNull(message = "Adresse ist ein Pflichtfeld")
        AdresseDTO adresse,

        @Valid
        @NotEmpty(message = "Die Speisekarte darf nicht leer sein")
        List<SpeiseDTO> speisen
) {
    public interface OnCreate {

    }
}
