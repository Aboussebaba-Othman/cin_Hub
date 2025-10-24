package com.cinhub.repository;

import com.cinhub.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.films WHERE c.idCategory = :id")
    Optional<Category> findByIdWithFilms(@Param("id") Long id);
    @Query("SELECT DISTINCT c FROM Category c INNER JOIN c.films")
    List<Category> findAllWithFilms();
    @Query("SELECT c FROM Category c WHERE c.films IS EMPTY")
    List<Category> findAllWithoutFilms();
}