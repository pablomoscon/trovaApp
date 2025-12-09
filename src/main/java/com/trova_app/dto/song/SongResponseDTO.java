package com.trova_app.dto.song;

import com.trova_app.model.Song;

public class SongResponseDTO {
    private Long id;
    private String name;
    private String duration;
    private String artistName;

    public SongResponseDTO(Long id, String name, String duration, String artistName) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.artistName = artistName;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {

        return duration;
    }

    public String getArtistName() {
        return artistName;
    }

    public static SongResponseDTO fromModel(Song song) {
        return new SongResponseDTO(
                song.getId(),
                song.getName(),
                song.getDuration(),
                song.getArtist().getName()
        );
    }
}
