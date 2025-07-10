package com.trovaApp.dto.album;

import com.trovaApp.dto.song.SongCreateDTO;

import java.util.List;

public class AlbumSeedDTO {
    private String title;
    private List<SongCreateDTO> listOfSongs;
    private String details;
    private String cdNumber;
    private String photo;
    private Integer year;
    private List<String> genres;
    private String artistName;
    private String displayArtistName;

    // Getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SongCreateDTO> getListOfSongs() {
        return listOfSongs;
    }

    public void setListOfSongs(List<SongCreateDTO> listOfSongs) {
        this.listOfSongs = listOfSongs;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDisplayArtistName() {
        return displayArtistName;
    }

    public void setDisplayArtistName(String displayArtistName) {
        this.displayArtistName = displayArtistName;
    }
}
