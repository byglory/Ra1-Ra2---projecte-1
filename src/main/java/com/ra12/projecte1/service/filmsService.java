package com.ra12.projecte1.service;

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
        return filmsRepository.updateById(Id, imagePath);
    }

    int addImagePath (String imgPath, Long Id){
        return filmsRepository.addImagePath(imgPath, Id);
    }


}
