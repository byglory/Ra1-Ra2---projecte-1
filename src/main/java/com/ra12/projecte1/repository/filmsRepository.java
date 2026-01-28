
package com.ra12.projecte1.repository;

import java.security.Timestamp;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ra12.projecte1.model.films;

public interface filmsRepository extends CrudRepository<films, Long>{
    @Modifying
    @Query("UPDATE films SET name = :name,  imagePath = :imagePath , year = :year, category = :category, dataUpdated = :dataUpdated WHERE id = :Id")
    int updateFilm(
        @Param("Id") Long Id, 
        @Param("name") String name, 
        @Param("imagePath") String imagePath, 
        @Param("year") int year,
        @Param("category") String category, 
        @Param("dataUpdated")Timestamp dataUpdated
    );

    @Modifying
    @Query("UPDATE films SET image_path = :imagePath, dataUpdated = :dataUpdated WHERE id = :Id")
    int addImagePath(
        @Param("imagePath") String imagePath, 
        @Param("Id") Long Id, 
        @Param("dataUpdated")Timestamp dataUpdated
    );
}
