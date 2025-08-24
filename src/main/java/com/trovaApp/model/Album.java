package com.trovaApp.model;

import com.trovaApp.enums.Genre;
import com.trovaApp.enums.Status;
import jakarta.persistence.*;
import java.util.Date;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Column(name = "apple_music_link", length = 500)
    private String appleMusicLink;

    @Column(name = "amazon_music_link", length = 500)
    private String amazonMusicLink;

    @Column(name = "spotify_link", length = 500)
    private String spotifyLink;

    public Album() {}

    public Album(String photo,
                 String title,
                 List<Song> listOfSongs,
                 String details,
                 String cdNumber,
                 Integer year,
                 Artist artist,
                 Set<Genre> genres,
                 String displayArtistName,
                 Date createdAt,
                 Status status,
                 String appleMusicLink,
                 String amazonMusicLink,
                 String spotifyLink
    ) {
        this.photo = photo;
        this.title = title;
        this.listOfSongs = listOfSongs;
        this.details = details;
        this.cdNumber = cdNumber;
        this.year = year;
        this.artist = artist;
        this.genres = genres;
        this.displayArtistName = displayArtistName;
        this.createdAt = createdAt;
        this.status = status;
        this.appleMusicLink = appleMusicLink;
        this.amazonMusicLink = amazonMusicLink;
        this.spotifyLink = spotifyLink;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
}
