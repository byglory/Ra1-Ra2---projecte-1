package com.ra12.projecte1.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ra12.projecte1.dto.filmRequestDTO;
import com.ra12.projecte1.dto.filmResponseDTO;
import com.ra12.projecte1.logging.CustomLogging;
import com.ra12.projecte1.model.films;
import com.ra12.projecte1.repository.filmsRepository;

/**
 * Capa de Servei: Conté tota la lògica de negoci.
 * Connecta el Controlador amb el Repositori.
 */
@Service
public class filmsService {

    @Autowired // Injecció de dependències del repositori 
    private filmsRepository filmsRepository;

    @Autowired // Injecció del nostre sistema de logs
    private CustomLogging customLogging;

    private final String CLASS_NAME = "FilmsService";

    // --- Itzan ---

    /**
     * [a1] Importa dades des d'un fitxer CSV (MultipartFile).
     * Llegeix línia per línia i guarda a la BBDD.
     */
    public void importFromCsv(MultipartFile file) {
        String methodName = "importFromCsv";
        
        if (file.isEmpty()) {
            customLogging.error(CLASS_NAME, methodName, "El fitxer CSV està buit.");
            return;
        }

        customLogging.info(CLASS_NAME, methodName, "Iniciant importació CSV: " + file.getOriginalFilename());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Validem que tingui almenys 4 camps
                if (data.length >= 4) {
                    try {
                        films film = new films(
                                Integer.parseInt(data[0].trim()), // Any
                                data[1].trim(), // Nom
                                data[2].trim(), // Categoria
                                data[3].trim()  // Imatge
                        );
                        filmsRepository.save(film); // Mètode CRUD estàndard
                        customLogging.info(CLASS_NAME, methodName, "Film importat: " + film.getName());
                    } catch (NumberFormatException e) {
                        customLogging.error(CLASS_NAME, methodName, "Error de format numèric a la línia: " + line);
                    }
                }
            }
        } catch (IOException e) {
            customLogging.error(CLASS_NAME, methodName, "Error de lectura IO: " + e.getMessage());
        }
    }

    /**
     * [b1] Crea un nou registre a partir d'un DTO.
     */
    public void createFilm(filmRequestDTO filmDTO) {
        String methodName = "createFilm";
        customLogging.info(CLASS_NAME, methodName, "Creant nova pel·lícula: " + filmDTO.getName());
        
        // Mapeig manual de DTO a Entitat
        films film = new films(
            filmDTO.getYear(),
            filmDTO.getName(),
            filmDTO.getCategory(),
            filmDTO.getImagePath()
        );
        filmsRepository.save(film);
        
        customLogging.info(CLASS_NAME, methodName, "Guardada a BBDD amb ID: " + film.getId());
    }

    /**
     * [c1] Llegeix tots els registres i els converteix a DTOs de resposta.
     */
    public List<filmResponseDTO> getAllFilms() {
        String methodName = "getAllFilms";
        customLogging.info(CLASS_NAME, methodName, "Consultant totes les pel·lícules");
        
        List<filmResponseDTO> response = new ArrayList<>();
        Iterable<films> films = filmsRepository.findAll();
        
        for (films f : films) {
            response.add(new filmResponseDTO(f.getId(), f.getName(), f.getCategory(), f.getYear(), f.getImagePath(), f.getUltimAcces()));
        }
        return response;
    }

    /**
     * [d1] Llegeix per ID utilitzant Optional.
     * Actualitza el camp 'ultimAcces' en llegir.
     */
    public Optional<filmResponseDTO> getFilmById(Long id) {
        String methodName = "getFilmById";
        customLogging.info(CLASS_NAME, methodName, "Buscant per ID: " + id);
        
        // Retorna Optional per evitar NullPointerException 
        Optional<films> filmOpt = filmsRepository.findById(id);
        
        if (filmOpt.isPresent()) {
            films f = filmOpt.get();
            f.setUltimAcces(LocalDateTime.now());
            filmsRepository.save(f); // Update de la data d'accés
            
            return Optional.of(new filmResponseDTO(f.getId(), f.getName(), f.getCategory(), f.getYear(), f.getImagePath(), f.getUltimAcces()));
        }
        
        customLogging.error(CLASS_NAME, methodName, "No s'ha trobat ID: " + id);
        return Optional.empty();
    }

    // --- Eric ---

    /**
     * [a2] Puja una imatge al servidor i actualitza la ruta a la BBDD.
     */
    public void uploadImage(Long id, MultipartFile file) {
        String methodName = "uploadImage";
        try {
            if (!filmsRepository.existsById(id)) { 
                customLogging.error(CLASS_NAME, methodName, "Film no trobat amb ID: " + id);
                return;
            }

            // Crear directori si no existeix
            String uploadDir = "uploads/";
            java.io.File directory = new java.io.File(uploadDir);
            if (!directory.exists()) directory.mkdirs();

            // Guardar fitxer físic
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            Path path = Paths.get(filePath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Ús de la QUERY personalitzada del repositori
            filmsRepository.updateImagePath(id, filePath, LocalDateTime.now());
            
            customLogging.info(CLASS_NAME, methodName, "Imatge pujada correctament per a ID: " + id);

        } catch (IOException e) {
            customLogging.error(CLASS_NAME, methodName, "Error guardant el fitxer: " + e.getMessage());
        }
    }

    /**
     * [b2] Actualitza un registre existent utilitzant la Query personalitzada.
     */
    public boolean updateFilm(Long id, filmRequestDTO filmDTO) {
        String methodName = "updateFilm";
        
        if (!filmsRepository.existsById(id)) {
            customLogging.error(CLASS_NAME, methodName, "Intent d'actualitzar ID inexistent: " + id);
            return false;
        }

        String imagePath = (filmDTO.getImagePath() != null) ? filmDTO.getImagePath() : "";
        
        // Cridem al @Query personalitzat
        int result = filmsRepository.updateFilm(
            id,
            filmDTO.getName(),
            imagePath,
            filmDTO.getYear(),
            filmDTO.getCategory(),
            LocalDateTime.now()
        );

        if (result > 0) {
            customLogging.info(CLASS_NAME, methodName, "Actualitzat correctament ID: " + id);
            return true;
        }
        return false;
    }

    /**
     * [c2] Esborra tots els registres de la taula.
     */
    public void deleteAllFilms() {
        String methodName = "deleteAllFilms";
        long count = filmsRepository.count(); 
        filmsRepository.deleteAll(); 
        customLogging.info(CLASS_NAME, methodName, "Eliminats " + count + " registres.");
    }

    /**
     * [d2] Esborra un registre per ID.
     */
    public boolean deleteFilmById(Long id) {
        String methodName = "deleteFilmById";
        if (filmsRepository.existsById(id)) {
            filmsRepository.deleteById(id); 
            customLogging.info(CLASS_NAME, methodName, "Eliminat ID: " + id);
            return true;
        }
        customLogging.error(CLASS_NAME, methodName, "Error eliminant, ID no trobat: " + id);
        return false;
    }
}