package com.ra12.projecte1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ra12.projecte1.dto.filmRequestDTO;
import com.ra12.projecte1.dto.filmResponseDTO;
import com.ra12.projecte1.service.filmsService;

/*
 * Controlador REST que exposa els endpoints de l'API.
 */
@RestController // Indica que és un controlador REST (retorna JSON)
@RequestMapping("api/films") // Prefix per a totes les URL d'aquesta class
public class filmsController {

    @Autowired // Injecta el servei
    private filmsService filmsService;

    // --- ENDPOINTS Itzan ---

    // Importar CSV: POST /api/films/import
    @PostMapping("/import")
    public String importCsv(@RequestParam() MultipartFile file) {
        filmsService.importFromCsv(file);
        return "Procés d'importació finalitzat. Revisa els logs.";
    }

    // Crear: POST /api/films
    @PostMapping
    public String createFilm(@RequestBody filmRequestDTO filmDTO) {
        filmsService.createFilm(filmDTO);
        return "Pel·lícula creada correctament.";
    }

    // Llegir Tots: GET /api/films
    @GetMapping
    public List<filmResponseDTO> getAllFilms() {
        return filmsService.getAllFilms();
    }

    // Llegir per ID: GET /api/films/{id}
    @GetMapping("/{id}")
    public filmResponseDTO getFilmById(@PathVariable Long id) {
        return filmsService.getFilmById(id)
                .orElseThrow(() -> new RuntimeException("Pel·lícula no trobada amb ID: " + id));
    }

    // --- ENDPOINTS Eric ---

    // Pujar imatge: POST /api/films/{id}/image
    @PostMapping("/{id}/image")
    public String uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        filmsService.uploadImage(id, file);
        return "Imatge processada per al film " + id;
    }

    // Actualitzar: PUT /api/films/{id}
    @PutMapping("/{id}")
    public String updateFilm(@PathVariable Long id, @RequestBody filmRequestDTO filmDTO) {
        boolean updated = filmsService.updateFilm(id, filmDTO);
        return updated ? "Pel·lícula actualitzada correctament." : "Error: ID no trobat.";
    }

    // Esborrar un: DELETE /api/films/{id}
    @DeleteMapping("/{id}")
    public String deleteFilm(@PathVariable Long id) {
        boolean deleted = filmsService.deleteFilmById(id);
        return deleted ? "Pel·lícula eliminada." : "Error: ID no trobat.";
    }

    // Esborrar tot: DELETE /api/films
    @DeleteMapping
    public String deleteAllFilms() {
        filmsService.deleteAllFilms();
        return "Tots els registres eliminats.";
    }
}