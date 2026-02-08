package com.ra12.projecte1.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private filmsLogging customLogging;

    private final String CLASS_NAME = "FilmsService";

    // [a1] i [e1] Importar CSV (Pendiente de implementar según tu código original)

    // [a2] i [e2] Afegir imatge a un registre existent
    @Transactional
    public void uploadImage(Long id, MultipartFile file) {
        String methodName = "uploadImage";
        try {
            if (!filmsRepository.existsById(id)) {
                customLogging.error(CLASS_NAME, methodName, "No s'ha trobat el film amb ID: " + id);
                return;
            }

            if (file.isEmpty()) {
                customLogging.error(CLASS_NAME, methodName, "El fitxer d'imatge està buit.");
                return;
            }

            String uploadDir = "uploads/";
            java.io.File directory = new java.io.File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            java.nio.file.Files.copy(file.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            filmsRepository.addImagePath(filePath, id, LocalDateTime.now());
            
            customLogging.info(CLASS_NAME, methodName, "Imatge pujada correctament per al film ID: " + id);

        } catch (IOException e) {
            customLogging.error(CLASS_NAME, methodName, "Error guardant la imatge: " + e.getMessage());
            throw new RuntimeException("Error al pujar la imatge", e);
        }
    }

    // [b2] i [e2] Update per ID
    @Transactional
    public boolean updateFilm(Long id, filmRequestDTO filmDTO) {
        String methodName = "updateFilm";
        Optional<films> existingFilmOpt = filmsRepository.findById(id);

        if (existingFilmOpt.isPresent()) {
            films currentFilm = existingFilmOpt.get();
            
            String imagePathToUse = currentFilm.getImagePath();
            if (filmDTO.getImagePath() != null && !filmDTO.getImagePath().isEmpty()) {
                imagePathToUse = filmDTO.getImagePath();
            }

            filmsRepository.updateFilm(
                id, 
                filmDTO.getName(), 
                imagePathToUse, 
                filmDTO.getYear(), 
                filmDTO.getCategory(), 
                LocalDateTime.now()
            );
            
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
        long count = filmsRepository.count(); 
        filmsRepository.deleteAll(); 
        customLogging.info(CLASS_NAME, methodName, "S'han eliminat tots els registres. Total: " + count);
    }

    // [d2] i [e2] Delete per ID
    public boolean deleteFilmById(Long id) {
        String methodName = "deleteFilmById";
        if (filmsRepository.existsById(id)) { 
            filmsRepository.deleteById(id); 
            customLogging.info(CLASS_NAME, methodName, "Film eliminat amb ID: " + id);
            return true;
        } else {
            customLogging.error(CLASS_NAME, methodName, "No es pot eliminar, ID no trobat: " + id);
            return false;
        }
    }
}