package com.cinhub.repository;

import com.cinhub.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

    List<Director> findByLastNameContainingIgnoreCase(String lastName);
    List<Director> findByFirstNameContainingIgnoreCase(String firstName);
    List<Director> findByNationality(String nationality);
    @Query("SELECT d FROM Director d WHERE LOWER(d.firstName) = LOWER(:firstName) AND LOWER(d.lastName) = LOWER(:lastName)")
    Optional<Director> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    @Query("SELECT d FROM Director d LEFT JOIN FETCH d.films WHERE d.idDirector = :id")
    Optional<Director> findByIdWithFilms(@Param("id") Long id);
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Director d WHERE LOWER(d.firstName) = LOWER(:firstName) AND LOWER(d.lastName) = LOWER(:lastName)")
    boolean existsByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    @Query("SELECT DISTINCT d FROM Director d INNER JOIN d.films")
    List<Director> findAllWithFilms();
    @Query("SELECT d FROM Director d WHERE d.films IS EMPTY")
    List<Director> findAllWithoutFilms();
}