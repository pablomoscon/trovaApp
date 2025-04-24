package com.trovaApp.dto.album;

import com.trovaApp.enums.Genre;

import java.util.Set;

public class AlbumPatchDTO {
    private String title;
    private String details;
    private String cdNumber;
    private String photo;
    private Integer year;
    private Long artistId;
    private Set<Genre> genres;
    private String displayArtistName;

    // Getters y Setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getCdNumber() { return cdNumber; }
    public void setCdNumber(String cdNumber) { this.cdNumber = cdNumber; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Long getArtistId() { return artistId; }
    public void setArtistId(Long artistId) { this.artistId = artistId; }

    public Set<Genre> getGenres() { return genres; }
    public void setGenres(Set<Genre> genres) { this.genres = genres; }

    public String getDisplayArtistName() { return displayArtistName; }
    public void setDisplayArtistName(String displayArtistName) { this.displayArtistName = displayArtistName; }
}
