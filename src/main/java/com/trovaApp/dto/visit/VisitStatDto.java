package com.trovaApp.dto.visit;

import java.time.LocalDateTime;

public class VisitStatDto {

    private Long albumId;
    private Long artistId;
    private String title;
    private String name;
    private int visits;
    private LocalDateTime visitTime;

    // Constructors
    public VisitStatDto() {
    }

    public VisitStatDto(Long albumId, Long artistId, String title, String name, int visits, LocalDateTime visitTime) {
        this.albumId = albumId;
        this.artistId = artistId;
        this.title = title;
        this.name = name;
        this.visits = visits;
        this.visitTime = visitTime;
    }

    // Getters and setters

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public LocalDateTime getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(LocalDateTime visitTime) {
        this.visitTime = visitTime;
    }
}
