package com.trovaApp.dto.song;

import com.trovaApp.model.Song;

public class SongCreateDTO {
    private Long id;
    private String name;
    private String duration;
    private String artistName;

    // Getters y setters
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public static SongCreateDTO fromSong(Song song) {
        SongCreateDTO dto = new SongCreateDTO();
        dto.setId(song.getId()); // <--- Asegurate de incluir esto
        dto.setName(song.getName());
        dto.setDuration(song.getDuration());
        dto.setArtistName(song.getArtist() != null ? song.getArtist().getName() : null);
        return dto;
    }
}
