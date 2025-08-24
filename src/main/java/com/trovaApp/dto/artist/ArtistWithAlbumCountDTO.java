package com.trovaApp.dto.artist;

import com.trovaApp.enums.Status;

import java.util.Date;

public class ArtistWithAlbumCountDTO {
    private Long id;
    private String name;
    private String details;
    private String nationality;
    private String photo;
    private Date createdAt;
    private Status status;
    private Long totalAlbums;

    public ArtistWithAlbumCountDTO(Long id, String name, String details, String nationality,
                                   String photo, Date createdAt, Status status, Long totalAlbums) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.nationality = nationality;
        this.photo = photo;
        this.createdAt = createdAt;
        this.status = status;
        this.totalAlbums = totalAlbums;
    }

    // getters & setters


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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getTotalAlbums() {
        return totalAlbums;
    }

    public void setTotalAlbums(Long totalAlbums) {
        this.totalAlbums = totalAlbums;
    }
}