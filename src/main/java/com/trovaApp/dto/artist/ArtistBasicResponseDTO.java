package com.trovaApp.dto.artist;

import com.trovaApp.enums.Status;
import com.trovaApp.model.Artist;

public class ArtistBasicResponseDTO {

    private Long id;
    private String name;
    private String details;
    private String nationality;
    private String photo;
    private Status status;

    public ArtistBasicResponseDTO() {
    }

    public ArtistBasicResponseDTO(Long id, String name, String details, String nationality, String photo, Status status) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.nationality = nationality;
        this.photo = photo;
        this.status = status;
    }

    // Factory method para mapear desde la entidad
    public static ArtistBasicResponseDTO fromModel(Artist artist) {
        return new ArtistBasicResponseDTO(
                artist.getId(),
                artist.getName(),
                artist.getDetails(),
                artist.getNationality(),
                artist.getPhoto(),
                artist.getStatus()
        );
    }

    // Getters y Setters

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

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }
}
