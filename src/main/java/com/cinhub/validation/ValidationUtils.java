package com.cinhub.validation;

import com.cinhub.exception.ValidationException;

import java.time.LocalDate;


public class ValidationUtils {


    public static void validateReleaseYear(Integer releaseYear) {
        int currentYear = LocalDate.now().getYear();
        if (releaseYear > currentYear) {
            throw new ValidationException("L'année de sortie ne peut pas être dans le futur");
        }
    }


    public static void validateRating(Double rating) {
        if (rating != null && (rating < 0.0 || rating > 10.0)) {
            throw new ValidationException("La note doit être comprise entre 0 et 10");
        }
    }


    public static void validateDuration(Integer duration) {
        if (duration != null && duration <= 0) {
            throw new ValidationException("La durée doit être supérieure à 0");
        }
    }

    public static void validateBirthDate(LocalDate birthDate) {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new ValidationException("La date de naissance doit être dans le passé");
        }
    }

    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " ne peut pas être vide");
        }
    }

    public static void validatePositiveId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new ValidationException(fieldName + " doit être un nombre positif");
        }
    }
}