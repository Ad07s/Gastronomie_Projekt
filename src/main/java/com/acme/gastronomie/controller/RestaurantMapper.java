package com.acme.gastronomie.controller;

import com.acme.gastronomie.entity.Adresse;
import com.acme.gastronomie.entity.Restaurant;
import com.acme.gastronomie.entity.Speise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

/// Mapper zwischen DTOs und Entity-Klassen für Restaurants.
/// Die Implementierung wird automatisch von MapStruct generiert.

@Mapper(nullValueIterableMappingStrategy = RETURN_DEFAULT, componentModel = "spring")
//@AnnotateWith(ExcludeFromJacocoGeneratedReport.class)
interface RestaurantMapper {
    /// Ein [RestaurantDTO] in eine [Restaurant] Entity konvertieren.
    ///
    /// @param dto Das Datenübertragungsobjekt
    /// @return Die Entity ohne ID (wird von der DB vergeben)
    @Mapping(target = "id", ignore = true)
    Restaurant toRestaurant(RestaurantDTO dto);

    /// Ein [AdresseDTO] in eine [Adresse] Entity konvertieren.
    Adresse toAdresse(AdresseDTO dto);

    /// Ein [SpeiseDTO] in eine [Speise] Entity konvertieren.
    Speise toSpeise(SpeiseDTO dto);


    /// Ein [Restaurant] in ein [RestaurantDTO] konvertieren.
    RestaurantDTO toDTO(Restaurant restaurant);

    /// Eine [Adresse] in ein [AdresseDTO] konvertieren.
    AdresseDTO toAdresseDTO(Adresse adresse);

    /// Eine [Speise] in ein [SpeiseDTO] konvertieren.
    SpeiseDTO toSpeiseDTO(Speise speise);

    RestaurantDTO DTO(Restaurant restaurant);
}
