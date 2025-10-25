package com.cinhub.service;

import com.cinhub.dto.FilmCreateDTO;
import com.cinhub.dto.FilmDTO;
import com.cinhub.dto.FilmUpdateDTO;
import com.cinhub.entity.Category;
import com.cinhub.entity.Director;
import com.cinhub.entity.Film;
import com.cinhub.exception.ResourceNotFoundException;
import com.cinhub.exception.ValidationException;
import com.cinhub.mapper.FilmMapper;
import com.cinhub.repository.CategoryRepository;
import com.cinhub.repository.DirectorRepository;
import com.cinhub.repository.FilmRepository;
import com.cinhub.validation.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FilmService {

    private static final Logger logger = LoggerFactory.getLogger(FilmService.class);

    private final FilmRepository filmRepository;
    private final DirectorRepository directorRepository;
    private final CategoryRepository categoryRepository;
    private final FilmMapper filmMapper;

    public FilmService(FilmRepository filmRepository,
                       DirectorRepository directorRepository,
                       CategoryRepository categoryRepository,
                       FilmMapper filmMapper) {
        this.filmRepository = filmRepository;
        this.directorRepository = directorRepository;
        this.categoryRepository = categoryRepository;
        this.filmMapper = filmMapper;
    }


    public FilmDTO createFilm(FilmCreateDTO createDTO) {
        logger.info("Création d'un nouveau film : {}", createDTO.getTitle());

        // Validations métier
        ValidationUtils.validateReleaseYear(createDTO.getReleaseYear());
        ValidationUtils.validateRating(createDTO.getRating());
        ValidationUtils.validateDuration(createDTO.getDuration());

        // Vérifier que le titre n'existe pas déjà
        if (filmRepository.existsByTitle(createDTO.getTitle())) {
            throw new ValidationException("Un film avec ce titre existe déjà");
        }

        // Récupérer le réalisateur
        Director director = directorRepository.findById(createDTO.getDirectorId())
                .orElseThrow(() -> new ResourceNotFoundException("Director", createDTO.getDirectorId()));

        // Récupérer la catégorie
        Category category = categoryRepository.findById(createDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", createDTO.getCategoryId()));

        // Créer et sauvegarder le film
        Film film = filmMapper.toEntity(createDTO, director, category);
        Film savedFilm = filmRepository.save(film);

        logger.info("Film créé avec succès : ID = {}", savedFilm.getIdFilm());
        return filmMapper.toDTO(savedFilm);
    }

    @Transactional(readOnly = true)
    public FilmDTO getFilmById(Long id) {
        logger.info("Récupération du film avec l'ID : {}", id);
        Film film = filmRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film", id));
        return filmMapper.toDTO(film);
    }

    @Transactional(readOnly = true)
    public List<FilmDTO> getAllFilms() {
        logger.info("Récupération de tous les films");
        return filmRepository.findAllWithDetails().stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FilmDTO updateFilm(Long id, FilmUpdateDTO updateDTO) {
        logger.info("Mise à jour du film avec l'ID : {}", id);

        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film", id));

        // Mettre à jour les champs si fournis
        if (updateDTO.getTitle() != null) {
            // Vérifier que le nouveau titre n'existe pas déjà
            if (!film.getTitle().equals(updateDTO.getTitle()) &&
                    filmRepository.existsByTitle(updateDTO.getTitle())) {
                throw new ValidationException("Un film avec ce titre existe déjà");
            }
            film.setTitle(updateDTO.getTitle());
        }

        if (updateDTO.getReleaseYear() != null) {
            ValidationUtils.validateReleaseYear(updateDTO.getReleaseYear());
            film.setReleaseYear(updateDTO.getReleaseYear());
        }

        if (updateDTO.getDuration() != null) {
            ValidationUtils.validateDuration(updateDTO.getDuration());
            film.setDuration(updateDTO.getDuration());
        }

        if (updateDTO.getSynopsis() != null) {
            film.setSynopsis(updateDTO.getSynopsis());
        }

        if (updateDTO.getRating() != null) {
            ValidationUtils.validateRating(updateDTO.getRating());
            film.setRating(updateDTO.getRating());
        }

        if (updateDTO.getDirectorId() != null) {
            Director director = directorRepository.findById(updateDTO.getDirectorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Director", updateDTO.getDirectorId()));
            film.setDirector(director);
        }

        if (updateDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", updateDTO.getCategoryId()));
            film.setCategory(category);
        }

        Film updatedFilm = filmRepository.save(film);
        logger.info("Film mis à jour avec succès : ID = {}", updatedFilm.getIdFilm());

        return filmMapper.toDTO(updatedFilm);
    }

    public void deleteFilm(Long id) {
        logger.info("Suppression du film avec l'ID : {}", id);

        if (!filmRepository.existsById(id)) {
            throw new ResourceNotFoundException("Film", id);
        }

        filmRepository.deleteById(id);
        logger.info("Film supprimé avec succès : ID = {}", id);
    }

    @Transactional(readOnly = true)
    public List<FilmDTO> searchFilmsByTitle(String title) {
        logger.info("Recherche de films par titre : {}", title);
        return filmRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<FilmDTO> searchFilmsByYear(Integer year) {
        logger.info("Recherche de films par année : {}", year);
        return filmRepository.findByReleaseYear(year).stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<FilmDTO> searchFilmsByCategory(Long categoryId) {
        logger.info("Recherche de films par catégorie : {}", categoryId);

        // Vérifier que la catégorie existe
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", categoryId);
        }

        return filmRepository.findByCategoryId(categoryId).stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FilmDTO> searchFilmsByMinRating(Double minRating) {
        logger.info("Recherche de films avec note >= {}", minRating);

        ValidationUtils.validateRating(minRating);

        return filmRepository.findByRatingGreaterThanEqual(minRating).stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FilmDTO> getFilmsByDirector(Long directorId) {
        logger.info("Récupération des films du réalisateur : {}", directorId);

        // Vérifier que le réalisateur existe
        if (!directorRepository.existsById(directorId)) {
            throw new ResourceNotFoundException("Director", directorId);
        }

        return filmRepository.findByDirectorId(directorId).stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }
}