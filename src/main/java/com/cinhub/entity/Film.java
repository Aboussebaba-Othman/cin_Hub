package com.cinhub.entity;

import javax.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "films")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFilm;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 1, max = 255, message = "Le titre doit contenir entre 1 et 255 caractères")
    @Column(nullable = false)
    private String title;

    @NotNull(message = "L'année de sortie est obligatoire")
    @Min(value = 1888, message = "L'année de sortie ne peut pas être avant 1888")
    @Max(value = 2100, message = "L'année de sortie ne peut pas être dans le futur")
    @Column(nullable = false)
    private Integer releaseYear;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée doit être supérieure à 0")
    @Column(nullable = false)
    private Integer duration;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @DecimalMin(value = "0.0", message = "La note doit être au minimum 0")
    @DecimalMax(value = "10.0", message = "La note doit être au maximum 10")
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id", nullable = false)
    @NotNull(message = "Le réalisateur est obligatoire")
    private Director director;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "La catégorie est obligatoire")
    private Category category;

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Film{" +
                "idFilm=" + idFilm +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", duration=" + duration +
                ", rating=" + rating +
                '}';
    }
}