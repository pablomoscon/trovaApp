package com.trovaApp.controller.artist;

import com.trovaApp.dto.artist.ArtistBasicResponseDTO;
import com.trovaApp.dto.artist.ArtistCreateDTO;
import com.trovaApp.dto.artist.ArtistFullResponseDTO;
import com.trovaApp.dto.artist.ArtistPatchDTO;
import com.trovaApp.exception.ArtistNotFoundException;
import com.trovaApp.model.Artist;
import com.trovaApp.service.artist.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/artist")

public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController (ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping
    public ResponseEntity<?> createArtist(@RequestBody ArtistCreateDTO dto) {
        Artist artist = artistService.create(dto);
        return new ResponseEntity<>(ArtistBasicResponseDTO.from(artist), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(
                artistService.findAll().stream().map(ArtistBasicResponseDTO::from).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Artist artist = artistService.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));
        return ResponseEntity.ok(ArtistFullResponseDTO.from(artist));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ArtistBasicResponseDTO> patch(@PathVariable Long id, @RequestBody ArtistPatchDTO dto) {
        Artist updated = artistService.patchArtist(id, dto);
        return ResponseEntity.ok(ArtistBasicResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        artistService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
