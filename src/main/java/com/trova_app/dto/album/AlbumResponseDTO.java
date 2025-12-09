package com.trova_app.dto.album;

import com.trova_app.dto.song.SongResponseDTO;
import com.trova_app.enums.Genre;
import com.trova_app.enums.Status;
import com.trova_app.model.Album;

import java.util.*;
import java.util.stream.Collectors;

public class AlbumResponseDTO {
    private Long id;
    private String title;
    private String details;
    private String cdNumber;
    private String photo;
    private Integer year;
    private Set<Genre> genres;
    private String artistName;
    private String displayArtistName;
    private Status status;
    private Date createdAt;
    private List<SongResponseDTO> listOfSongs;
    private String appleMusicLink;
    private String amazonMusicLink;
    private String spotifyLink;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Set<Genre> getGenres() { return genres; }
    public void setGenres(Set<Genre> genres) { this.genres = genres; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getDisplayArtistName() { return displayArtistName; }
    public void setDisplayArtistName(String displayArtistName) { this.displayArtistName = displayArtistName; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getAppleMusicLink() { return appleMusicLink; }
    public void setAppleMusicLink(String appleMusicLink) { this.appleMusicLink = appleMusicLink; }

    public String getAmazonMusicLink() { return amazonMusicLink; }
    public void setAmazonMusicLink(String amazonMusicLink) { this.amazonMusicLink = amazonMusicLink; }

    public String getSpotifyLink() { return spotifyLink; }
    public void setSpotifyLink(String spotifyLink) { this.spotifyLink = spotifyLink; }

    // Convert Album entity to DTO
    public static AlbumResponseDTO fromModel(Album album) {
        AlbumResponseDTO dto = new AlbumResponseDTO();

        dto.setId(album.getId());
        dto.setTitle(album.getTitle());
        dto.setDetails(album.getDetails());
        dto.setCdNumber(album.getCdNumber());
        dto.setPhoto(album.getPhoto());
        dto.setYear(album.getYear());
        dto.setStatus(album.getStatus());
        dto.setCreatedAt(album.getCreatedAt());

        dto.setGenres(album.getGenres());
        dto.setDisplayArtistName(album.getDisplayArtistName());
        dto.setArtistName(album.getArtist() != null ? album.getArtist().getName() : null);

        dto.setAppleMusicLink(album.getAppleMusicLink());
        dto.setAmazonMusicLink(album.getAmazonMusicLink());
        dto.setSpotifyLink(album.getSpotifyLink());

        return dto;
    }
}
