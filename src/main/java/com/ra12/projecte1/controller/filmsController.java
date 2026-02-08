package com.ra12.projecte1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ra12.projecte1.dto.filmRequestDTO;
import com.ra12.projecte1.service.filmsService;

@RestController 
@RequestMapping("api/films") 
public class filmsController {

    @Autowired // [cite: 100]
    private filmsService filmsService;

    // [a1] Endpoint per afegir info via CSV (passant el path del fitxer com a text)
// [a2] Endpoint per afegir imatge a un registre (POST amb MultipartFile)
    @PostMapping("/{id}/image")
    public String uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            filmsService.uploadImage(id, file);
            return "Imatge pujada correctament al film " + id;
        } catch (Exception e) {
            return "Error al pujar la imatge: " + e.getMessage();
        }
    }

    // [b2] Update per ID (PUT)
    @PutMapping("/{id}")
    public String updateFilm(@PathVariable Long id, @RequestBody filmRequestDTO filmDTO) {
        boolean updated = filmsService.updateFilm(id, filmDTO);
        if (updated) {
            return "Pel·lícula actualitzada correctament.";
        } else {
            return "Error: No s'ha trobat la pel·lícula amb ID " + id;
        }
    }

    // [c2] Delete all (DELETE)
    @DeleteMapping
    public String deleteAllFilms() {
        filmsService.deleteAllFilms();
        return "Tots els registres han estat eliminats.";
    }

    // [d2] Delete per ID (DELETE /{id})
    @DeleteMapping("/{id}") // [cite: 111]
    public String deleteFilm(@PathVariable Long id) { // [cite: 112]
        boolean deleted = filmsService.deleteFilmById(id); // [cite: 113]
        if (deleted) {
            return "Pel·lícula eliminada correctament.";
        } else {
            return "Error: No s'ha trobat la pel·lícula amb ID " + id;
        }
    }
}