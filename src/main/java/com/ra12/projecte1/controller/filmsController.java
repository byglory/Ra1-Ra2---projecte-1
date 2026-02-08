package com.ra12.projecte1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ra12.projecte1.dto.filmRequestDTO;
import com.ra12.projecte1.dto.filmResponseDTO;
import com.ra12.projecte1.service.filmsService;

@RestController 
@RequestMapping("api/films") 
public class filmsController {

    @Autowired // [cite: 100]
    private filmsService filmsService;

    // [a1] Endpoint per afegir info via CSV (passant el path del fitxer com a text)
    @PostMapping("/import")
    public String importCsv(@RequestBody MultipartFile csv) {
        filmsService.importFromCsv(csv);
        return "Procés d'importació finalitzat. Revisa els logs.";
    }

    // [b1] Crear nou registre
    @PostMapping // Mètode POST estàndard
    public String createFilm(@RequestBody filmRequestDTO filmDTO) {
        filmsService.createFilm(filmDTO);
        return "Pel·lícula creada correctament";
    }

    // [c1] Read all registres
    @GetMapping // (sense ID llista tot)
    public List<filmResponseDTO> getAllFilms() {
        return filmsService.getAllFilms();
    }

    // [d1] Read per id
    @GetMapping("/{id}")
    public filmResponseDTO getFilmById(@PathVariable Long id) { 
        Optional<filmResponseDTO> film = filmsService.getFilmById(id);
        // Gestió de l'Optional per evitar NullPointerException 
        if (film.isPresent()) {
            return film.get();
        } else {
            // Retornem null o podríem llançar una excepció (simplificat aquí)
            throw new RuntimeException("Pel·lícula amb id " + id + " no trobada");
        }
    }
}
