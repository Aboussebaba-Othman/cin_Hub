package com.cinhub.controller;

import com.cinhub.dto.FilmCreateDTO;
import com.cinhub.dto.FilmDTO;
import com.cinhub.dto.FilmUpdateDTO;
import com.cinhub.service.FilmService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<FilmDTO> createFilm(@Valid @RequestBody FilmCreateDTO createDTO) {
        logger.info("POST /api/films - Création d'un film");
        FilmDTO createdFilm = filmService.createFilm(createDTO);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<FilmDTO> getFilmById(@PathVariable Long id) {
        logger.info("GET /api/films/{} - Récupération d'un film", id);
        FilmDTO film = filmService.getFilmById(id);
        return ResponseEntity.ok(film);
    }


    @GetMapping
    public ResponseEntity<List<FilmDTO>> getAllFilms() {
        logger.info("GET /api/films - Récupération de tous les films");
        List<FilmDTO> films = filmService.getAllFilms();
        return ResponseEntity.ok(films);
    }


    @PutMapping("/{id}")
    public ResponseEntity<FilmDTO> updateFilm(
            @PathVariable Long id,
            @Valid @RequestBody FilmUpdateDTO updateDTO) {
        logger.info("PUT /api/films/{} - Mise à jour d'un film", id);
        FilmDTO updatedFilm = filmService.updateFilm(id, updateDTO);
        return ResponseEntity.ok(updatedFilm);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteFilm(@PathVariable Long id) {
        logger.info("DELETE /api/films/{} - Suppression d'un film", id);
        filmService.deleteFilm(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Film supprimé avec succès");
        response.put("id", id.toString());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/search/title")
    public ResponseEntity<List<FilmDTO>> searchFilmsByTitle(@RequestParam String q) {
        logger.info("GET /api/films/search/title?q={}", q);
        List<FilmDTO> films = filmService.searchFilmsByTitle(q);
        return ResponseEntity.ok(films);
    }


    @GetMapping("/search/year")
    public ResponseEntity<List<FilmDTO>> searchFilmsByYear(@RequestParam Integer year) {
        logger.info("GET /api/films/search/year?year={}", year);
        List<FilmDTO> films = filmService.searchFilmsByYear(year);
        return ResponseEntity.ok(films);
    }


    @GetMapping("/search/category/{categoryId}")
    public ResponseEntity<List<FilmDTO>> searchFilmsByCategory(@PathVariable Long categoryId) {
        logger.info("GET /api/films/search/category/{}", categoryId);
        List<FilmDTO> films = filmService.searchFilmsByCategory(categoryId);
        return ResponseEntity.ok(films);
    }


    @GetMapping("/search/rating")
    public ResponseEntity<List<FilmDTO>> searchFilmsByMinRating(@RequestParam Double min) {
        logger.info("GET /api/films/search/rating?min={}", min);
        List<FilmDTO> films = filmService.searchFilmsByMinRating(min);
        return ResponseEntity.ok(films);
    }


    @GetMapping("/director/{directorId}")
    public ResponseEntity<List<FilmDTO>> getFilmsByDirector(@PathVariable Long directorId) {
        logger.info("GET /api/films/director/{}", directorId);
        List<FilmDTO> films = filmService.getFilmsByDirector(directorId);
        return ResponseEntity.ok(films);
    }
}