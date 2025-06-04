package com.trovaApp.dto.artist;

import com.trovaApp.dto.album.AlbumResponseDTO;
import com.trovaApp.enums.Status;
import com.trovaApp.model.Artist;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class ArtistFullResponseDTO {

    private Long id;
    private String name;
    private String details;
    private String nationality;
    private String photo;
    private Date createdAt;
    private Set<AlbumResponseDTO> albums;
    private Status status;

    public ArtistFullResponseDTO() {
    }

    public ArtistFullResponseDTO(Long id,
                                 String name,
                                 String details,
                                 String nationality,
                                 String photo,
                                 Set<AlbumResponseDTO> albums,
                                 Date createdAt,
                                 Status status
    ) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.nationality = nationality;
        this.photo = photo;
        this.albums = albums;
        this.createdAt = createdAt;
        this.status = status;
    }

    // Factory method para mapear desde la entidad

    public static ArtistFullResponseDTO from(Artist artist) {
        Set<AlbumResponseDTO> albumDTOs = artist.getAlbums().stream().map(
                        AlbumResponseDTO::fromModel).collect(Collectors.toSet());

        return new ArtistFullResponseDTO(
                artist.getId(),
                artist.getName(),
                artist.getDetails(),
                artist.getNationality(),
                artist.getPhoto(),
                albumDTOs,
                artist.getCreatedAt(),
                artist.getStatus());
    }

    // Getters y Setters

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
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

    public void setDetails(String details)
    {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
