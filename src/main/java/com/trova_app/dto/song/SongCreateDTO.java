package com.trova_app.dto.song;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trova_app.model.Song;

public class SongCreateDTO {

    private String name;
    private String duration;
    private String artistName;

    @JsonIgnore
    private Long id;

    // Getters y setters

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
        dto.setName(song.getName());
        dto.setDuration(song.getDuration());
        dto.setArtistName(song.getArtist() != null ? song.getArtist().getName() : null);
        return dto;
    }
}
