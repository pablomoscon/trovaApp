package com.trovaApp.controller.song;

import com.trovaApp.dto.song.SongPatchDTO;
import com.trovaApp.dto.song.SongResponseDTO;
import com.trovaApp.model.Song;
import com.trovaApp.service.song.SongService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("songs")
public class SongController {

    private final SongService songService;

    @Autowired
    public SongController (SongService songService) {
        this.songService = songService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Song song = songService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found"));
        return ResponseEntity.ok(song);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchSong(@PathVariable Long id, @RequestBody SongPatchDTO patchDTO) {
        Song updatedSong = songService.patchSong(id, patchDTO);
        return ResponseEntity.ok(SongResponseDTO.fromModel(updatedSong));
    }
}
