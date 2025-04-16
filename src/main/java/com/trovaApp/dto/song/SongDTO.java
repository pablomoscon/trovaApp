package com.trovaApp.dto.song;

import com.trovaApp.model.Song;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SongDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 200, message = "Name must be between 1 and 200 characters")
    private String name;

    @NotBlank(message = "Duration is required")
    @Pattern(
            regexp = "^\\d{1,2}h\\s*\\d{1,2}m$",
            message = "Duration must follow the format like '1h 30m' or '0h 45m'"
    )
    private String duration;

    @NotBlank(message = "Artist name is required")
    @Size(max = 100, message = "Artist name must be at most 100 characters")
    private String artistName;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    // Conversión desde entidad Song
    public static SongDTO fromSong(Song song) {
        SongDTO dto = new SongDTO();
        dto.setId(song.getId());
        dto.setName(song.getName());
        dto.setDuration(song.getDuration());
        dto.setArtistName(song.getArtist() != null ? song.getArtist().getName() : null);
        return dto;
    }
}
