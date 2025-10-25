package com.cinhub.service;

import com.cinhub.dto.DirectorCreateDTO;
import com.cinhub.dto.DirectorDTO;
import com.cinhub.dto.FilmDTO;
import com.cinhub.entity.Director;
import com.cinhub.exception.BusinessRuleException;
import com.cinhub.exception.ResourceNotFoundException;
import com.cinhub.exception.ValidationException;
import com.cinhub.mapper.DirectorMapper;
import com.cinhub.mapper.FilmMapper;
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
public class DirectorService {

    private static final Logger logger = LoggerFactory.getLogger(DirectorService.class);

    private final DirectorRepository directorRepository;
    private final FilmRepository filmRepository;
    private final DirectorMapper directorMapper;
    private final FilmMapper filmMapper;

    public DirectorService(DirectorRepository directorRepository,
                           FilmRepository filmRepository,
                           DirectorMapper directorMapper,
                           FilmMapper filmMapper) {
        this.directorRepository = directorRepository;
        this.filmRepository = filmRepository;
        this.directorMapper = directorMapper;
        this.filmMapper = filmMapper;
    }


    public DirectorDTO createDirector(DirectorCreateDTO createDTO) {
        logger.info("Création d'un nouveau réalisateur : {} {}",
                createDTO.getFirstName(), createDTO.getLastName());

        // Validation de la date de naissance
        ValidationUtils.validateBirthDate(createDTO.getBirthDate());

        // Vérifier que le réalisateur n'existe pas déjà
        if (directorRepository.existsByFullName(createDTO.getFirstName(), createDTO.getLastName())) {
            throw new ValidationException("Un réalisateur avec ce nom existe déjà");
        }

        Director director = directorMapper.toEntity(createDTO);
        Director savedDirector = directorRepository.save(director);

        logger.info("Réalisateur créé avec succès : ID = {}", savedDirector.getIdDirector());
        return directorMapper.toDTO(savedDirector);
    }


    @Transactional(readOnly = true)
    public DirectorDTO getDirectorById(Long id) {
        logger.info("Récupération du réalisateur avec l'ID : {}", id);
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director", id));
        return directorMapper.toDTO(director);
    }

    @Transactional(readOnly = true)
    public List<DirectorDTO> getAllDirectors() {
        logger.info("Récupération de tous les réalisateurs");
        return directorRepository.findAll().stream()
                .map(directorMapper::toDTO)
                .collect(Collectors.toList());
    }


    public DirectorDTO updateDirector(Long id, DirectorCreateDTO updateDTO) {
        logger.info("Mise à jour du réalisateur avec l'ID : {}", id);

        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Director", id));

        // Validation de la date de naissance
        if (updateDTO.getBirthDate() != null) {
            ValidationUtils.validateBirthDate(updateDTO.getBirthDate());
        }

        // Vérifier que le nouveau nom n'existe pas déjà (sauf si c'est le même)
        if (updateDTO.getFirstName() != null && updateDTO.getLastName() != null) {
            boolean isDifferentName = !director.getFirstName().equals(updateDTO.getFirstName()) ||
                    !director.getLastName().equals(updateDTO.getLastName());

            if (isDifferentName &&
                    directorRepository.existsByFullName(updateDTO.getFirstName(), updateDTO.getLastName())) {
                throw new ValidationException("Un réalisateur avec ce nom existe déjà");
            }
        }

        // Mettre à jour les champs
        if (updateDTO.getFirstName() != null) {
            director.setFirstName(updateDTO.getFirstName());
        }
        if (updateDTO.getLastName() != null) {
            director.setLastName(updateDTO.getLastName());
        }
        if (updateDTO.getNationality() != null) {
            director.setNationality(updateDTO.getNationality());
        }
        if (updateDTO.getBirthDate() != null) {
            director.setBirthDate(updateDTO.getBirthDate());
        }
        if (updateDTO.getBiography() != null) {
            director.setBiography(updateDTO.getBiography());
        }

        Director updatedDirector = directorRepository.save(director);
        logger.info("Réalisateur mis à jour avec succès : ID = {}", updatedDirector.getIdDirector());

        return directorMapper.toDTO(updatedDirector);
    }


    public void deleteDirector(Long id) {
        logger.info("Suppression du réalisateur avec l'ID : {}", id);

        if (!directorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Director", id);
        }

        // Vérifier qu'il n'a pas de films associés
        Long filmCount = filmRepository.countByDirectorId(id);
        if (filmCount > 0) {
            throw new BusinessRuleException(
                    String.format("Impossible de supprimer le réalisateur : il a %d film(s) associé(s). " +
                            "Veuillez d'abord supprimer ou réassigner ses films.", filmCount));
        }

        directorRepository.deleteById(id);
        logger.info("Réalisateur supprimé avec succès : ID = {}", id);
    }


    @Transactional(readOnly = true)
    public List<FilmDTO> getDirectorFilmography(Long directorId) {
        logger.info("Récupération de la filmographie du réalisateur : {}", directorId);

        if (!directorRepository.existsById(directorId)) {
            throw new ResourceNotFoundException("Director", directorId);
        }

        return filmRepository.findByDirectorId(directorId).stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<DirectorDTO> searchDirectorsByLastName(String lastName) {
        logger.info("Recherche de réalisateurs par nom : {}", lastName);
        return directorRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(directorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DirectorDTO> searchDirectorsByFirstName(String firstName) {
        logger.info("Recherche de réalisateurs par prénom : {}", firstName);
        return directorRepository.findByFirstNameContainingIgnoreCase(firstName).stream()
                .map(directorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DirectorDTO> searchDirectorsByNationality(String nationality) {
        logger.info("Recherche de réalisateurs par nationalité : {}", nationality);
        return directorRepository.findByNationality(nationality).stream()
                .map(directorMapper::toDTO)
                .collect(Collectors.toList());
    }
}