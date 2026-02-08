package com.ra12.projecte1.dto;
public class filmRequestDTO {

    private int year;
    private String name;
    private String category;
    private String imagePath;

    // Constructor buit
    public filmRequestDTO() {}

    // --- GETTERS I SETTERS ---

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}