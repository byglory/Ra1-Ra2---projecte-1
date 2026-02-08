package com.ra12.projecte1.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Classe Model que representa la taula 'films' a la Base de Dades.
 *
 */
@Entity // Indica que aquesta classe és una entitat JPA
@Table(name = "films") // Defineix el nom de la taula
public class films {

    @Id // Marca el camp com a clau primària (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // L'ID es genera automàticament (Auto-increment)
    private Long id;

    private int year;
    private String name;
    private String category;
    private String imagePath;
    
    // Camps per a l'auditoria i logs de dades
    private LocalDateTime ultimAcces;
    private LocalDateTime dataCreated;
    private LocalDateTime dataUpdated;

    // Constructor buit obligatori per a JPA
    public films() {}

    // Constructor per crear nous objectes fàcilment
    public films(int year, String name, String category, String imagePath) {
        this.year = year;
        this.name = name;
        this.category = category;
        this.imagePath = imagePath;
        this.dataCreated = LocalDateTime.now();
        this.dataUpdated = LocalDateTime.now();
    }

    // --- Getters i Setters --- 
    // Necessaris perquè JPA i Jackson (JSON) puguin accedir a les propietats

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public LocalDateTime getUltimAcces() { return ultimAcces; }
    public void setUltimAcces(LocalDateTime ultimAcces) { this.ultimAcces = ultimAcces; }

    public LocalDateTime getDataCreated() { return dataCreated; }
    public void setDataCreated(LocalDateTime dataCreated) { this.dataCreated = dataCreated; }

    public LocalDateTime getDataUpdated() { return dataUpdated; }
    public void setDataUpdated(LocalDateTime dataUpdated) { this.dataUpdated = dataUpdated; }

    @Override
    public String toString() { // Per visualitzar l'objecte com a String
        return "films{id=" + id + ", name='" + name + "'}";
    }
}