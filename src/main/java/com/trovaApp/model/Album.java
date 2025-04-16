package com.trovaApp.model;

import com.trovaApp.enums.Genre;
import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Song> listOfSongs;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Lob
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "cd_number", nullable = false, unique = true)
    private String cdNumber;

    @Column(name = "photo", nullable = false)
    private String photo;

    @Column(name = "year", nullable = false)
    private Integer year;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "album_genres", joinColumns = @JoinColumn(name = "album_id"))
    @Column(name = "genre")
    @Enumerated(EnumType.STRING)
    private Set<Genre> genres;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "display_artist_name", length = 200)
    private String displayArtistName;

    public Album() {}

    public Album(String photo, String title, List<Song> listOfSongs, String details, String cdNumber, Integer year, Artist artist, Set<Genre> genres, String displayArtistName) {
        this.photo = photo;
        this.title = title;
        this.listOfSongs = listOfSongs;
        this.details = details;
        this.cdNumber = cdNumber;
        this.year = year;
        this.artist = artist;
        this.genres = genres;
        this.displayArtistName = displayArtistName;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    // Getters y Setters
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

    public List<Song> getListOfSongs() {
        return listOfSongs;
    }

    public void setListOfSongs(List<Song> listOfSongs) {
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getDisplayArtistName() {
        return displayArtistName;
    }

    public void setDisplayArtistName(String displayArtistName) {
        this.displayArtistName = displayArtistName;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}