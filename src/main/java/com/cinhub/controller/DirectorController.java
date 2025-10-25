package com.cinhub.controller;

import com.cinhub.dto.DirectorCreateDTO;
import com.cinhub.dto.DirectorDTO;
import com.cinhub.dto.FilmDTO;
import com.cinhub.service.DirectorService;
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
@RequestMapping("/directors")
public class DirectorController {

    private static final Logger logger = LoggerFactory.getLogger(DirectorController.class);

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }


    @PostMapping
    public ResponseEntity<DirectorDTO> createDirector(@Valid @RequestBody DirectorCreateDTO createDTO) {
        logger.info("POST /api/directors - Création d'un réalisateur");
        DirectorDTO createdDirector = directorService.createDirector(createDTO);
        return new ResponseEntity<>(createdDirector, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DirectorDTO> getDirectorById(@PathVariable Long id) {
        logger.info("GET /api/directors/{} - Récupération d'un réalisateur", id);
        DirectorDTO director = directorService.getDirectorById(id);
        return ResponseEntity.ok(director);
    }

    @GetMapping
    public ResponseEntity<List<DirectorDTO>> getAllDirectors() {
        logger.info("GET /api/directors - Récupération de tous les réalisateurs");
        List<DirectorDTO> directors = directorService.getAllDirectors();
        return ResponseEntity.ok(directors);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DirectorDTO> updateDirector(
            @PathVariable Long id,
            @Valid @RequestBody DirectorCreateDTO updateDTO) {
        logger.info("PUT /api/directors/{} - Mise à jour d'un réalisateur", id);
        DirectorDTO updatedDirector = directorService.updateDirector(id, updateDTO);
        return ResponseEntity.ok(updatedDirector);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDirector(@PathVariable Long id) {
        logger.info("DELETE /api/directors/{} - Suppression d'un réalisateur", id);
        directorService.deleteDirector(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Réalisateur supprimé avec succès");
        response.put("id", id.toString());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/filmography")
    public ResponseEntity<List<FilmDTO>> getDirectorFilmography(@PathVariable Long id) {
        logger.info("GET /api/directors/{}/filmography - Récupération de la filmographie", id);
        List<FilmDTO> films = directorService.getDirectorFilmography(id);
        return ResponseEntity.ok(films);
    }


    @GetMapping("/search/lastname")
    public ResponseEntity<List<DirectorDTO>> searchByLastName(@RequestParam String q) {
        logger.info("GET /api/directors/search/lastname?q={}", q);
        List<DirectorDTO> directors = directorService.searchDirectorsByLastName(q);
        return ResponseEntity.ok(directors);
    }

    @GetMapping("/search/firstname")
    public ResponseEntity<List<DirectorDTO>> searchByFirstName(@RequestParam String q) {
        logger.info("GET /api/directors/search/firstname?q={}", q);
        List<DirectorDTO> directors = directorService.searchDirectorsByFirstName(q);
        return ResponseEntity.ok(directors);
    }


    @GetMapping("/search/nationality")
    public ResponseEntity<List<DirectorDTO>> searchByNationality(@RequestParam String q) {
        logger.info("GET /api/directors/search/nationality?q={}", q);
        List<DirectorDTO> directors = directorService.searchDirectorsByNationality(q);
        return ResponseEntity.ok(directors);
    }
}