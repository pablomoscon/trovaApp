package com.trovaApp.dto.artist;

public class ArtistPatchDTO {

    private String name;
    private String details;
    private String nationality;
    private String photo;

    public ArtistPatchDTO() {
    }

    public ArtistPatchDTO(String name, String details, String nationality, String photo) {
        this.name = name;
        this.details = details;
        this.nationality = nationality;
        this.photo = photo;
    }

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
