package com.trova_app.dto.album;

import com.trova_app.enums.Genre;
import com.trova_app.enums.Status;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class AlbumPatchDTO {

    // Optional title (2-100 characters)
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    // Optional details (up to 500 characters)
    @Size(max = 500, message = "Details can have at most 500 characters")
    private String details;

    // Optional CD number (up to 20 characters)
    @Size(max = 20, message = "CD number must have at most 20 characters")
    private String cdNumber;

    // Optional cover photo (multipart file)
    private MultipartFile photo;

    // Optional release year (must be between 1900 and 2100)
    @Min(value = 1900, message = "Year must be greater than or equal to 1900")
    @Max(value = 2100, message = "Year must be less than or equal to 2100")
    private Integer year;

    // Optional name to display as artist (2-100 characters)
    @Size(min = 2, max = 100, message = "Display artist name must be between 2 and 100 characters")
    private String displayArtistName;

    // Optional artist ID (must be a positive number)
    @Positive(message = "Artist ID must be a positive number")
    private Long artistId;

    // Optional set of genres (must not contain null values and have at least one item if present)
    @Size(min = 1, message = "At least one genre must be provided if genres are set")
    private Set<@NotNull(message = "Genres must not contain null values") Genre> genres;

    // Optional Apple Music link (up to 500 characters)
    @Size(max = 500, message = "Apple Music link can have at most 500 characters")
    private String appleMusicLink;

    // Optional Amazon Music link (up to 500 characters)
    @Size(max = 500, message = "Amazon Music link can have at most 500 characters")
    private String amazonMusicLink;

    // Optional Spotify link (up to 500 characters)
    @Size(max = 500, message = "Spotify link can have at most 500 characters")
    private String spotifyLink;

    // Optional album status (e.g., ACTIVE, SUSPENDED)
    private Status status;

    // Getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCdNumber() {
        return cdNumber;
    }

    public void setCdNumber(String cdNumber) {
        this.cdNumber = cdNumber;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDisplayArtistName() {
        return displayArtistName;
    }

    public void setDisplayArtistName(String displayArtistName) {
        this.displayArtistName = displayArtistName;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public String getAppleMusicLink() {
        return appleMusicLink;
    }

    public void setAppleMusicLink(String appleMusicLink) {
        this.appleMusicLink = appleMusicLink;
    }

    public String getAmazonMusicLink() {
        return amazonMusicLink;
    }

    public void setAmazonMusicLink(String amazonMusicLink) {
        this.amazonMusicLink = amazonMusicLink;
    }

    public String getSpotifyLink() {
        return spotifyLink;
    }

    public void setSpotifyLink(String spotifyLink) {
        this.spotifyLink = spotifyLink;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
