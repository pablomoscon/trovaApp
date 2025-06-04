package com.trovaApp.controller.artist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.artist.*;
import com.trovaApp.exception.ArtistNotFoundException;
import com.trovaApp.model.Artist;
import com.trovaApp.service.artist.ArtistService;
import com.trovaApp.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/artist")

public class ArtistController {

    private final ArtistService artistService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ArtistController (
            ArtistService artistService,
            ObjectMapper objectMapper
    ) {
        this.artistService = artistService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> createArtist(     @RequestParam("artist") String artistJson,
                                               @RequestPart("photo") MultipartFile photo
    ) throws JsonProcessingException {
        FileUtils.validateImageFile(photo);

        ArtistCreateDTO artistDTO = objectMapper.readValue(artistJson, ArtistCreateDTO.class);
        Artist artist = artistService.create(artistDTO, photo);
        return new ResponseEntity<>(ArtistBasicResponseDTO.from(artist), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ArtistBasicResponseDTO>> getArtists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Page<ArtistBasicResponseDTO> artists = artistService.findAll(page, size);
        return ResponseEntity.ok(artists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistBasicResponseDTO> findById(@PathVariable Long id) {
        Artist artist = artistService.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));
        return ResponseEntity.ok(ArtistBasicResponseDTO.from(artist));
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
