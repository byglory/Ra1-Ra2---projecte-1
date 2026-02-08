package com.ra12.projecte1.repository;

import java.time.LocalDateTime; 

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ra12.projecte1.model.films;

public interface filmsRepository extends CrudRepository<films, Long>{

    @Modifying
    @Transactional 
    @Query("UPDATE films f SET f.name = :name, f.imagePath = :imagePath, f.year = :year, f.category = :category, f.dataUpdated = :dataUpdated WHERE f.id = :Id")
    int updateFilm(
        @Param("Id") Long Id, 
        @Param("name") String name, 
        @Param("imagePath") String imagePath, 
        @Param("year") int year,
        @Param("category") String category, 
        @Param("dataUpdated") LocalDateTime dataUpdated 
    );

    @Modifying
    @Transactional
    @Query("UPDATE films f SET f.imagePath = :imagePath, f.dataUpdated = :dataUpdated WHERE f.id = :Id")
    int addImagePath(
        @Param("imagePath") String imagePath, 
        @Param("Id") Long Id, 
        @Param("dataUpdated") LocalDateTime dataUpdated
    );
}