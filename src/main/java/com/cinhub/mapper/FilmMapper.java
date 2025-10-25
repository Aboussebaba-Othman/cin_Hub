package com.cinhub.mapper;

import com.cinhub.dto.FilmCreateDTO;
import com.cinhub.dto.FilmDTO;
import com.cinhub.entity.Category;
import com.cinhub.entity.Director;
import com.cinhub.entity.Film;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {

    private final DirectorMapper directorMapper;
    private final CategoryMapper categoryMapper;

    public FilmMapper(DirectorMapper directorMapper, CategoryMapper categoryMapper) {
        this.directorMapper = directorMapper;
        this.categoryMapper = categoryMapper;
    }


    public FilmDTO toDTO(Film film) {
        if (film == null) {
            return null;
        }

        return FilmDTO.builder()
                .idFilm(film.getIdFilm())
                .title(film.getTitle())
                .releaseYear(film.getReleaseYear())
                .duration(film.getDuration())
                .synopsis(film.getSynopsis())
                .rating(film.getRating())
                .director(directorMapper.toDTO(film.getDirector()))
                .category(categoryMapper.toDTO(film.getCategory()))
                .build();
    }


    public Film toEntity(FilmCreateDTO dto, Director director, Category category) {
        if (dto == null) {
            return null;
        }

        return Film.builder()
                .title(dto.getTitle())
                .releaseYear(dto.getReleaseYear())
                .duration(dto.getDuration())
                .synopsis(dto.getSynopsis())
                .rating(dto.getRating())
                .director(director)
                .category(category)
                .build();
    }
}