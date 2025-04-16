package com.trovaApp.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Lob
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "photo", columnDefinition = "TEXT") // <- Nuevo campo
    private String photo;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Album> albums;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Song> songs;

    public Artist() {
    }

    public Artist(String name, String details, String nationality, String photo) {
        this.name = name;
        this.details = details;
        this.nationality = nationality;
        this.photo = photo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
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

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }
}
