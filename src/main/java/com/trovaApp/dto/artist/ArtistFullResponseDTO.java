package com.trovaApp.dto.artist;

import com.trovaApp.dto.album.AlbumResponseDTO;
import com.trovaApp.model.Artist;

import java.util.Set;
import java.util.stream.Collectors;

public class ArtistFullResponseDTO {

    private Long id;
    private String name;
    private String details;
    private String nationality;
    private String photo;
    private Set<AlbumResponseDTO> albums;

    public ArtistFullResponseDTO() {
    }

    public ArtistFullResponseDTO(Long id, String name, String details, String nationality, String photo, Set<AlbumResponseDTO> albums) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.nationality = nationality;
        this.photo = photo;
        this.albums = albums;
    }

    // Factory method para mapear desde la entidad

    public static ArtistFullResponseDTO from(Artist artist) {
        Set<AlbumResponseDTO> albumDTOs = artist.getAlbums()
                .stream()
                .map(AlbumResponseDTO::fromModel)
                .collect(Collectors.toSet());

        return new ArtistFullResponseDTO(
                artist.getId(),
                artist.getName(),
                artist.getDetails(),
                artist.getNationality(),
                artist.getPhoto(),
                albumDTOs
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

    public Set<AlbumResponseDTO> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<AlbumResponseDTO> albums) {
        this.albums = albums;
    }
}
