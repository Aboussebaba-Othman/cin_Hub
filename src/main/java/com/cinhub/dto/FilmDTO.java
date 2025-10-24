package com.cinhub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmDTO {
    private Long idFilm;
    private String title;
    private Integer releaseYear;
    private Integer duration;
    private String synopsis;
    private Double rating;
    private DirectorDTO director;
    private CategoryDTO category;
}