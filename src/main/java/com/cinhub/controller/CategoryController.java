package com.cinhub.controller;

import com.cinhub.dto.CategoryCreateDTO;
import com.cinhub.dto.CategoryDTO;
import com.cinhub.dto.FilmDTO;
import com.cinhub.service.CategoryService;
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
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryCreateDTO createDTO) {
        logger.info("POST /api/categories - Création d'une catégorie");
        CategoryDTO createdCategory = categoryService.createCategory(createDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        logger.info("GET /api/categories/{} - Récupération d'une catégorie", id);
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }


    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        logger.info("GET /api/categories - Récupération de toutes les catégories");
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryCreateDTO updateDTO) {
        logger.info("PUT /api/categories/{} - Mise à jour d'une catégorie", id);
        CategoryDTO updatedCategory = categoryService.updateCategory(id, updateDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        logger.info("DELETE /api/categories/{} - Suppression d'une catégorie", id);
        categoryService.deleteCategory(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Catégorie supprimée avec succès");
        response.put("id", id.toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/films")
    public ResponseEntity<List<FilmDTO>> getCategoryFilms(@PathVariable Long id) {
        logger.info("GET /api/categories/{}/films - Récupération des films de la catégorie", id);
        List<FilmDTO> films = categoryService.getCategoryFilms(id);
        return ResponseEntity.ok(films);
    }


    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchCategoriesByName(@RequestParam String q) {
        logger.info("GET /api/categories/search?q={}", q);
        List<CategoryDTO> categories = categoryService.searchCategoriesByName(q);
        return ResponseEntity.ok(categories);
    }
}