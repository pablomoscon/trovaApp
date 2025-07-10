package com.trovaApp.dto.album;

import com.trovaApp.enums.Genre;
import com.trovaApp.enums.Status;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;  // IMPORTAR MultipartFile

import java.util.Set;

public class AlbumPatchDTO {

    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @Size(max = 500, message = "Details can have at most 500 characters")
    private String details;

    @Size(max = 20, message = "CD number must have at most 20 characters")
    private String cdNumber;

    private MultipartFile photo;

    @Min(value = 1900, message = "Year must be greater than or equal to 1900")
    @Max(value = 2100, message = "Year must be less than or equal to 2100")
    private Integer year;

    @Size(min = 2, max = 100, message = "Display artist name must be between 2 and 100 characters")
    private String displayArtistName;

    @Positive(message = "Artist ID must be a positive number")
    private Long artistId;

    @Size(min = 1, message = "At least one genre must be provided if genres are set")
    private Set<@NotNull(message = "Genres must not contain null values") Genre> genres;

    private Status status;


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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
