package com.ra12.projecte1.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader; // Importar el logger
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ra12.projecte1.dto.filmRequestDTO;
import com.ra12.projecte1.dto.filmResponseDTO;
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
                if (data.length >= 4) {
                    try {
                        films film = new films(
                                Integer.parseInt(data[0].trim()),
                                data[1].trim(),
                                data[2].trim(),
                                data[3].trim()
                        );
                        filmsRepository.save(film);
                        customLogging.info(CLASS_NAME, methodName, "Film importat correctament: " + film.getName());
                    } catch (NumberFormatException e) {
                        customLogging.error(CLASS_NAME, methodName, "Format d'any invàlid a la línia: " + line);
                    }
                }
            }
        } catch (IOException e) {
            customLogging.error(CLASS_NAME, methodName, "Error IO en processar el fitxer: " + e.getMessage());
        }
    }

    // [b1] i [e1] Crear nou registre
    public void createFilm(filmRequestDTO filmDTO) {
        String methodName = "createFilm";
        customLogging.info(CLASS_NAME, methodName, "Rebuda petició per crear pel·lícula: " + filmDTO.getName());
        
        films film = new films(
            filmDTO.getYear(),
            filmDTO.getName(),
            filmDTO.getCategory(),
            filmDTO.getImagePath()
        );
        filmsRepository.save(film);
        
        customLogging.info(CLASS_NAME, methodName, "Pel·lícula guardada a BBDD amb ID: " + film.getId());
    }

    // [c1] i [e1] Read All
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

    // [d1] i [e1] Read per ID
    public Optional<filmResponseDTO> getFilmById(Long id) {
        String methodName = "getFilmById";
        customLogging.info(CLASS_NAME, methodName, "Buscant pel·lícula amb ID: " + id);
        
        Optional<films> filmOpt = filmsRepository.findById(id);
        
        if (filmOpt.isPresent()) {
            films f = filmOpt.get();
            f.setUltimAcces(LocalDateTime.now());
            filmsRepository.save(f);
            
            customLogging.info(CLASS_NAME, methodName, "Pel·lícula trobada i actualitzat últim accés: " + f.getName());
            return Optional.of(new filmResponseDTO(f.getId(), f.getName(), f.getCategory(), f.getYear(), f.getImagePath(), f.getUltimAcces()));
        }
        
        customLogging.error(CLASS_NAME, methodName, "Pel·lícula no trobada amb ID: " + id);
        return Optional.empty();
    }
}