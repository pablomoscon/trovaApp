package com.trovaApp.dto.album;

import com.trovaApp.dto.song.SongCreateDTO;
import com.trovaApp.enums.Genre;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

public class AlbumCreateDTO {

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotEmpty(message = "Album must contain at least one song")
    @Valid
    private List<@Valid SongCreateDTO> listOfSongs;

    @Size(max = 500, message = "Details can have at most 500 characters")
    private String details;

    @Size(max = 20, message = "CD number must have at most 20 characters")
    private String cdNumber;

    @Pattern(
            regexp = "^(https?://.*|/.*)?$",
            message = "Photo must be a valid URL or a relative path"
    )
    private String photo;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year must be greater than or equal to 1900")
    @Max(value = 2100, message = "Year must be less than or equal to 2100")
    private Integer year;

    @NotNull(message = "Artist ID is required")
    private Long artistId;

    @NotEmpty(message = "At least one genre is required")
    private Set<Genre> genres;

    @NotBlank(message = "Display artist name is required")
    @Size(min = 2, max = 100, message = "Display artist name must be between 2 and 100 characters")
    private String displayArtistName;

    // Getters y setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<SongCreateDTO> getListOfSongs() { return listOfSongs; }
    public void setListOfSongs(List<SongCreateDTO> listOfSongs) { this.listOfSongs = listOfSongs; }

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
