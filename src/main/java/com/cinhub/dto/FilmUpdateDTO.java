package com.cinhub.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmUpdateDTO {

    @Size(min = 1, max = 255, message = "Le titre doit contenir entre 1 et 255 caractères")
    private String title;

    @Min(value = 1888, message = "L'année de sortie ne peut pas être avant 1888")
    @Max(value = 2100, message = "L'année de sortie ne peut pas être dans le futur")
    private Integer releaseYear;

    @Min(value = 1, message = "La durée doit être supérieure à 0")
    private Integer duration;

    private String synopsis;

    @DecimalMin(value = "0.0", message = "La note doit être au minimum 0")
    @DecimalMax(value = "10.0", message = "La note doit être au maximum 10")
    private Double rating;

    private Long directorId;

    private Long categoryId;
}