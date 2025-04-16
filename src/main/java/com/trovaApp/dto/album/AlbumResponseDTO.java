package com.trovaApp.dto.album;

import com.trovaApp.dto.song.SongDTO;
import com.trovaApp.enums.Genre;
import com.trovaApp.model.Album;
import com.trovaApp.model.Song;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlbumResponseDTO {
    private Long id;
    private String title;
    private String details;
    private String cdNumber;
    private String photo;
    private Integer year;
    private List<SongDTO> listOfSongs;  // Cambio de Set<String> a Set<SongDTO>
    private Set<Genre> genres;
    private String artistName;
    private String displayArtistName;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<SongDTO> getListOfSongs() {
        return listOfSongs;
    }

    public void setListOfSongs(List<SongDTO> listOfSongs) {
        this.listOfSongs = listOfSongs;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
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

    public static AlbumResponseDTO fromModel(Album album) {
        AlbumResponseDTO dto = new AlbumResponseDTO();
        dto.setId(album.getId());
        dto.setTitle(album.getTitle());
        dto.setDetails(album.getDetails());
        dto.setCdNumber(album.getCdNumber());
        dto.setPhoto(album.getPhoto());
        dto.setYear(album.getYear());

        List<SongDTO> songDTOs = new ArrayList<>();
        Set<Long> songIds = new HashSet<>(); // Usamos Set para evitar duplicados

        Long albumArtistId = album.getArtist() != null ? album.getArtist().getId() : null;

        for (Song song : album.getListOfSongs()) {
            if (song.getArtist() != null &&
                    song.getArtist().getId().equals(albumArtistId) &&
                    song.getId() != null &&
                    songIds.add(song.getId())) { // add devuelve false si ya estaba
                songDTOs.add(SongDTO.fromSong(song));
            }
        }

        dto.setListOfSongs(songDTOs);

        dto.setGenres(album.getGenres());
        dto.setDisplayArtistName(album.getDisplayArtistName());
        dto.setArtistName(album.getArtist() != null ? album.getArtist().getName() : null);
        return dto;
    }
}