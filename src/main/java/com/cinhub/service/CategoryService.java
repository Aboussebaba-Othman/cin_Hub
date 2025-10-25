package com.cinhub.service;

import com.cinhub.dto.CategoryCreateDTO;
import com.cinhub.dto.CategoryDTO;
import com.cinhub.dto.FilmDTO;
import com.cinhub.entity.Category;
import com.cinhub.exception.BusinessRuleException;
import com.cinhub.exception.ResourceNotFoundException;
import com.cinhub.exception.ValidationException;
import com.cinhub.mapper.CategoryMapper;
import com.cinhub.mapper.FilmMapper;
import com.cinhub.repository.CategoryRepository;
import com.cinhub.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final FilmRepository filmRepository;
    private final CategoryMapper categoryMapper;
    private final FilmMapper filmMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           FilmRepository filmRepository,
                           CategoryMapper categoryMapper,
                           FilmMapper filmMapper) {
        this.categoryRepository = categoryRepository;
        this.filmRepository = filmRepository;
        this.categoryMapper = categoryMapper;
        this.filmMapper = filmMapper;
    }

    public CategoryDTO createCategory(CategoryCreateDTO createDTO) {
        logger.info("Création d'une nouvelle catégorie : {}", createDTO.getName());

        // Vérifier que la catégorie n'existe pas déjà
        if (categoryRepository.existsByName(createDTO.getName())) {
            throw new ValidationException("Une catégorie avec ce nom existe déjà");
        }

        Category category = categoryMapper.toEntity(createDTO);
        Category savedCategory = categoryRepository.save(category);

        logger.info("Catégorie créée avec succès : ID = {}", savedCategory.getIdCategory());
        return categoryMapper.toDTO(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        logger.info("Récupération de la catégorie avec l'ID : {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        return categoryMapper.toDTO(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        logger.info("Récupération de toutes les catégories");
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO updateCategory(Long id, CategoryCreateDTO updateDTO) {
        logger.info("Mise à jour de la catégorie avec l'ID : {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        // Vérifier que le nouveau nom n'existe pas déjà (sauf si c'est le même)
        if (updateDTO.getName() != null &&
                !category.getName().equals(updateDTO.getName()) &&
                categoryRepository.existsByName(updateDTO.getName())) {
            throw new ValidationException("Une catégorie avec ce nom existe déjà");
        }

        // Mettre à jour les champs
        if (updateDTO.getName() != null) {
            category.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            category.setDescription(updateDTO.getDescription());
        }

        Category updatedCategory = categoryRepository.save(category);
        logger.info("Catégorie mise à jour avec succès : ID = {}", updatedCategory.getIdCategory());

        return categoryMapper.toDTO(updatedCategory);
    }


    public void deleteCategory(Long id) {
        logger.info("Suppression de la catégorie avec l'ID : {}", id);

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }

        // Vérifier qu'elle n'a pas de films associés
        Long filmCount = filmRepository.countByCategoryId(id);
        if (filmCount > 0) {
            throw new BusinessRuleException(
                    String.format("Impossible de supprimer la catégorie : elle contient %d film(s). " +
                            "Veuillez d'abord supprimer ou recatégoriser ces films.", filmCount));
        }

        categoryRepository.deleteById(id);
        logger.info("Catégorie supprimée avec succès : ID = {}", id);
    }

    @Transactional(readOnly = true)
    public List<FilmDTO> getCategoryFilms(Long categoryId) {
        logger.info("Récupération des films de la catégorie : {}", categoryId);

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", categoryId);
        }

        return filmRepository.findByCategoryId(categoryId).stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<CategoryDTO> searchCategoriesByName(String name) {
        logger.info("Recherche de catégories par nom : {}", name);
        return categoryRepository.findByNameContainingIgnoreCase(name).stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}