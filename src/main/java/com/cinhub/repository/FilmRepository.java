package com.cinhub.repository;

import com.cinhub.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findByTitle(String title);
    List<Film> findByTitleContainingIgnoreCase(String title);
    List<Film> findByReleaseYear(Integer releaseYear);
    @Query("SELECT f FROM Film f WHERE f.director.idDirector = :directorId")
    List<Film> findByDirectorId(@Param("directorId") Long directorId);
    @Query("SELECT f FROM Film f WHERE f.category.idCategory = :categoryId")
    List<Film> findByCategoryId(@Param("categoryId") Long categoryId);
    @Query("SELECT f FROM Film f WHERE f.rating >= :minRating")
    List<Film> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);
    @Query("SELECT f FROM Film f WHERE f.releaseYear BETWEEN :startYear AND :endYear")
    List<Film> findByReleaseYearBetween(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);
    @Query("SELECT f FROM Film f JOIN FETCH f.director JOIN FETCH f.category WHERE f.idFilm = :id")
    Optional<Film> findByIdWithDetails(@Param("id") Long id);
    @Query("SELECT DISTINCT f FROM Film f JOIN FETCH f.director JOIN FETCH f.category")
    List<Film> findAllWithDetails();
    boolean existsByTitle(String title);

    @Query("SELECT COUNT(f) FROM Film f WHERE f.director.idDirector = :directorId")
    Long countByDirectorId(@Param("directorId") Long directorId);
    @Query("SELECT COUNT(f) FROM Film f WHERE f.category.idCategory = :categoryId")
    Long countByCategoryId(@Param("categoryId") Long categoryId);
}