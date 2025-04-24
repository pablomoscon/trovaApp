package com.trovaApp.dto.song;

import com.trovaApp.model.Song;

public class SongResponseDTO {
    private Long id;
    private String title;
    private String duration;

    public SongResponseDTO(Long id, String title, String duration) {
        this.id = id;
        this.title = title;
        this.duration = duration;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public static SongResponseDTO fromModel(Song song) {
        return new SongResponseDTO(
                song.getId(),
                song.getName(),
                song.getDuration()
        );
    }
}
