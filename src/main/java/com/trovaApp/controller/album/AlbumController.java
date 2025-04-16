package com.trovaApp.controller.album;

import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.album.AlbumResponseDTO;
import com.trovaApp.dto.artist.ArtistFullResponseDTO;
import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.service.album.AlbumService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")

public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController (AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping
    public ResponseEntity<AlbumResponseDTO> createAlbum(@RequestBody AlbumCreateDTO dto) {
        Album createdAlbum = albumService.create(dto);
        return new ResponseEntity<>(AlbumResponseDTO.fromModel(createdAlbum), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> findAll() {
        List<Album> albums = albumService.findAll();
        List<AlbumResponseDTO> dtoList = albums.stream()
                .map(AlbumResponseDTO::fromModel)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Album album = albumService.getAlbumWithSongs(id);
        return ResponseEntity.ok(AlbumResponseDTO.fromModel(album));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> patch(@PathVariable Long id, @RequestBody AlbumPatchDTO dto) {
        Album updatedAlbum = albumService.patchAlbum(id, dto);
        return ResponseEntity.ok(AlbumResponseDTO.fromModel(updatedAlbum));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        albumService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
