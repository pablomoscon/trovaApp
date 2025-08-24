package com.trovaApp.controller.song;

import com.trovaApp.dto.song.SongPatchDTO;
import com.trovaApp.dto.song.SongResponseDTO;
import com.trovaApp.model.Song;
import com.trovaApp.service.song.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "songs", description = "Operations related to Songs")
@RestController
@RequestMapping("songs")
public class SongController {

    private final SongService songService;

    @Autowired
    public SongController (SongService songService) {
        this.songService = songService;
    }

    @Operation(summary = "Get a song by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Song song = songService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found"));
        return ResponseEntity.ok(song);
    }

    @Operation(summary = "Update partial fields of a song")
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchSong(@PathVariable Long id, @RequestBody SongPatchDTO patchDTO) {
        Song updatedSong = songService.patchSong(id, patchDTO);
        return ResponseEntity.ok(SongResponseDTO.fromModel(updatedSong));
    }

    @Operation(summary = "Delete multiple songs by their IDs")
    @DeleteMapping
    public ResponseEntity<Void> deleteSongs(@RequestBody List<Long> ids) {
        try {
            songService.deleteByIds(ids);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
