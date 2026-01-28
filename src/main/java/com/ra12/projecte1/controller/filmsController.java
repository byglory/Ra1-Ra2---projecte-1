package com.ra12.projecte1.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ra12.projecte1.model.films;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api")
public class filmsController {

    @GetMapping("/getFilms")
    public ResponseEntity<List<films>> getAllFilms(@RequestParam String param) {
        List<films> films = 
        return ResponseEntity.status(HttpStatus.OK).body(films);
    }
    
}
