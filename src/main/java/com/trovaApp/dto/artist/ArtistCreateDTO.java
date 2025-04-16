package com.trovaApp.dto.artist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ArtistCreateDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Details can have at most 500 characters")
    private String details;

    @Size(max = 100, message = "Nationality can have at most 100 characters")
    private String nationality;

    @Pattern(
            regexp = "^(https?://.*|/.*)?$",
            message = "Photo must be a valid URL or a relative path"
    )
    private String photo; // <- Nuevo campo con validación opcional

    public ArtistCreateDTO() {
    }

    public ArtistCreateDTO(String name, String details, String nationality, String photo) {
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
