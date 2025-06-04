package com.trovaApp.dto.artist;

import com.trovaApp.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class ArtistCreateDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Details can have at most 500 characters")
    private String details;

    @NotBlank(message = "Nationality is required")
    @Size(max = 100, message = "Nationality can have at most 100 characters")
    private String nationality;

    @NotBlank(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Status is required")
    private Status status;

    public ArtistCreateDTO() {
    }

    public ArtistCreateDTO(String name, String details, String nationality, Status status) {
        this.name = name;
        this.details = details;
        this.nationality = nationality;
        this.status = status;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }
}
