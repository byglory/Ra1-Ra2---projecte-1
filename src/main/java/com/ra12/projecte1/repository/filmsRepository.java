package com.ra12.projecte1.repository;
import java.security.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ra12.projecte1.model.films;

public interface filmsRepository extends CrudRepository<films, Long>{
    @Query("UPDATE users SET name = ?, description = ?, email = ?, password = ?, image_path = ?, dataUpdated = ? WHERE id = ?")
    int updateFilm(@Param("Id") Long Id, @Param("name") String name, @Param("imagePath") String imagePath, 



    @Query("UPDATE users SET image_path = :imagePath, dataUpdated = ? WHERE id =")
    int addImagePath(@Param("imagePath") String imagePath, @Param("Id") Long Id, @Param("dataUpdated")Timestamp dataUpdated);
}
