package com.ra12.projecte1.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional; // Importar el logger

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ra12.projecte1.dto.filmRequestDTO;
import com.ra12.projecte1.logging.filmsLogging;
import com.ra12.projecte1.model.films;
import com.ra12.projecte1.repository.filmsRepository;

@Service
public class filmsService {

    @Autowired
    private filmsRepository filmsRepository;

    @Autowired
    private filmsLogging customLogging; // Injecció del logger

    private final String CLASS_NAME = "FilmsService"; // Constant per no repetir el nom

    // [a1] i [e1] Importar CSV
// [a2] i [e2] Afegir imatge a un registre existent
    public void uploadImage(Long id, MultipartFile file) {
        String methodName = "uploadImage";
        try {
            Optional<films> filmOpt = filmsRepository.findById(id); // [cite: 87]
            if (filmOpt.isEmpty()) {
                customLogging.error(CLASS_NAME, methodName, "No s'ha trobat el film amb ID: " + id);
                return;
            }

            if (file.isEmpty()) {
                customLogging.error(CLASS_NAME, methodName, "El fitxer d'imatge està buit.");
                return;
            }

            // Lògica per guardar el fitxer al disc
            // Creem una carpeta "uploads" si no existeix
            String uploadDir = "uploads/";
            java.io.File directory = new java.io.File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generem un nom únic per evitar sobreescriure
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            
            // Guardem el fitxer físicament
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            java.nio.file.Files.copy(file.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            // Actualitzem l'entitat amb la ruta de la imatge
            films film = filmOpt.get();
            film.setImagePath(filePath);
            film.setDataUpdated(LocalDateTime.now());
            
            // Guardem els canvis a la BBDD (save actua com update si té ID) [cite: 88]
            filmsRepository.save(film);
            
            customLogging.info(CLASS_NAME, methodName, "Imatge pujada correctament per al film ID: " + id);

        } catch (IOException e) {
            customLogging.error(CLASS_NAME, methodName, "Error guardant la imatge: " + e.getMessage());
            throw new RuntimeException("Error al pujar la imatge", e);
        }
    }

    // [b2] i [e2] Update per ID
    public boolean updateFilm(Long id, filmRequestDTO filmDTO) {
        String methodName = "updateFilm";
        // Busquem si existeix [cite: 87]
        Optional<films> existingFilmOpt = filmsRepository.findById(id);

        if (existingFilmOpt.isPresent()) {
            films film = existingFilmOpt.get();
            
            // Actualitzem els camps amb les noves dades
            film.setName(filmDTO.getName());
            film.setCategory(filmDTO.getCategory());
            film.setYear(filmDTO.getYear());
            // Si ve una ruta d'imatge al DTO, l'actualitzem, si no, mantenim l'anterior
            if (filmDTO.getImagePath() != null && !filmDTO.getImagePath().isEmpty()) {
                film.setImagePath(filmDTO.getImagePath());
            }
            film.setDataUpdated(LocalDateTime.now());

            // save() fa un UPDATE perquè l'objecte ja té un ID [cite: 88]
            filmsRepository.save(film);
            
            customLogging.info(CLASS_NAME, methodName, "Film actualitzat correctament: " + id);
            return true;
        } else {
            customLogging.error(CLASS_NAME, methodName, "Intent d'actualitzar film inexistent ID: " + id);
            return false;
        }
    }

    // [c2] i [e2] Delete all registres
    public void deleteAllFilms() {
        String methodName = "deleteAllFilms";
        long count = filmsRepository.count(); // [cite: 82]
        filmsRepository.deleteAll(); // Operació estàndard de CrudRepository [cite: 52]
        customLogging.info(CLASS_NAME, methodName, "S'han eliminat tots els registres. Total: " + count);
    }

    // [d2] i [e2] Delete per ID
    public boolean deleteFilmById(Long id) {
        String methodName = "deleteFilmById";
        if (filmsRepository.existsById(id)) { // [cite: 80]
            filmsRepository.deleteById(id); // [cite: 83]
            customLogging.info(CLASS_NAME, methodName, "Film eliminat amb ID: " + id);
            return true;
        } else {
            customLogging.error(CLASS_NAME, methodName, "No es pot eliminar, ID no trobat: " + id);
            return false;
        }
    }
}