package com.ra12.projecte1.service;

import java.security.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;

import com.ra12.projecte1.repository.filmsRepository;

public class filmsService {

    @Autowired filmsRepository filmsRepository;

    void DeleteById (Long Id){
        filmsRepository.deleteById(Id);
    }

    void DeleteAll(){
        filmsRepository.deleteAll();
    }

    int UpdateById (Long Id, String imagePath){
        Timestamp dataUpdated =  
        return filmsRepository.updateFilm(Id,name,imagePath,dataUpdated)
    }

    int addImagePath (String imgPath, Long Id){
        return filmsRepository.addImagePath(imgPath, Id);
    }


}
