package com.ra12.projecte1.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ra12.projecte1.model.films;

/*
 * Interfície Repositori que estén de CrudRepository.
 * Spring genera automàticament la implementació de les operacions CRUD bàsiques.
 */
@Repository
public interface filmsRepository extends CrudRepository<films, Long> {

    /*
     * UPDATE personalitzat utilitzant JPQL (Java Persistence Query Language).
     * S'usa @Modifying i @Transactional perquè és una operació d'escriptura.
     */
    @Modifying
    @Transactional
    @Query("UPDATE films f SET f.name = :name, f.imagePath = :imagePath, f.year = :year, f.category = :category, f.dataUpdated = :dataUpdated WHERE f.id = :id")
    int updateFilm(
        @Param("id") Long id,
        @Param("name") String name,
        @Param("imagePath") String imagePath,
        @Param("year") int year,
        @Param("category") String category,
        @Param("dataUpdated") LocalDateTime dataUpdated
    );

    /*
     * UPDATE específic només per actualitzar la ruta de la imatge.
     */
    @Modifying
    @Transactional
    @Query("UPDATE films f SET f.imagePath = :imagePath, f.dataUpdated = :dataUpdated WHERE f.id = :id")
    int updateImagePath(
        @Param("id") Long id,
        @Param("imagePath") String imagePath,
        @Param("dataUpdated") LocalDateTime dataUpdated
    );
}