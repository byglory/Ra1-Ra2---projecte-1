package com.ra12.projecte1.dto;

import java.time.LocalDateTime;

public class filmResponseDTO {

    private Long id;
    private String name;
    private String category;
    private int year;
    private String imagePath;
    private LocalDateTime ultimAcces;

    // Constructor complet per facilitar la conversió
    public filmResponseDTO(Long id, String name, String category, int year, String imagePath, LocalDateTime ultimAcces) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.year = year;
        this.imagePath = imagePath;
        this.ultimAcces = ultimAcces;
    }

    // --- GETTERS I SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getUltimAcces() {
        return ultimAcces;
    }

    public void setUltimAcces(LocalDateTime ultimAcces) {
        this.ultimAcces = ultimAcces;
    }
}