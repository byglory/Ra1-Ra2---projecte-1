package com.ra12.projecte1.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository; 

import com.ra12.projecte1.model.films;

@Repository
public interface filmsRepository extends CrudRepository<films, Long> {
    // Spring genera automàticament la implementació CRUD
    // Podríem afegir mètodes personalitzats com findByName si fos necessari 
}