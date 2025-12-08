package com.trovaApp.controller.artist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trovaApp.dto.artist.*;
import com.trovaApp.enums.Status;
import com.trovaApp.exception.ArtistNotFoundException;
import com.trovaApp.model.Artist;
import com.trovaApp.service.artist.ArtistService;
import com.trovaApp.service.visit.VisitService;
import com.trovaApp.util.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "artists", description = "Operations related to Artists")
@RestController
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService artistService;
    private final VisitService visitService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ArtistController(
            ArtistService artistService,
            VisitService visitService,
            ObjectMapper objectMapper
    ) {
        this.artistService = artistService;
        this.objectMapper = objectMapper;
        this.visitService = visitService;
    }

    @Operation(summary = "Create a new artist with photo")
    @PostMapping
    public ResponseEntity<ArtistBasicResponseDTO> createArtist(@RequestParam("artist") String artistJson,
                                          @RequestPart("photo") MultipartFile photo
    ) throws JsonProcessingException {
        FileUtils.validateImageFile(photo);

        ArtistCreateDTO artistDTO = objectMapper.readValue(artistJson, ArtistCreateDTO.class);
        Artist artist = artistService.create(artistDTO, photo);
        return new ResponseEntity<>(ArtistBasicResponseDTO.fromModel(artist), HttpStatus.CREATED);
    }

    @Operation(summary = "Get paginated list of artists")
    @GetMapping
    public ResponseEntity<Page<ArtistBasicResponseDTO>> getArtists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) Status status) {

        Page<Artist> artistPage = artistService.findAll(page, size, status);
        Page<ArtistBasicResponseDTO> dtoPage = artistPage.map(ArtistBasicResponseDTO::fromModel);

        return ResponseEntity.ok(dtoPage);
    }

    @Operation(summary = "Get a paginated list of artists with album count")
    @GetMapping("/summary")
    public ResponseEntity<Page<ArtistWithAlbumCountDTO>> getArtistsWithAlbumCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Status status
    ) {
        Page<ArtistWithAlbumCountDTO> artistsPage = artistService.finArtistWithAlbumCount(page, size, status);
        return ResponseEntity.ok(artistsPage);
    }

    @Operation(summary = "Search artists by query")
    @GetMapping("/search")
    public ResponseEntity<Page<ArtistBasicResponseDTO>> searchArtists(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) Status status) {

        Page<Artist> artistPage = artistService.search(q, page, size, status);
        Page<ArtistBasicResponseDTO> dtoPage = artistPage.map(ArtistBasicResponseDTO::fromModel);
        return ResponseEntity.ok(dtoPage);
    }

    @Operation(summary = "Get paginated artists with their albums")
    @GetMapping("/with-albums")
    public ResponseEntity<Page<ArtistFullResponseDTO>> findAllWithAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Artist> artistsPage = artistService.findAllWithAlbums(pageable);

        Page<ArtistFullResponseDTO> dtoPage = artistsPage.map(ArtistFullResponseDTO::from);

        return ResponseEntity.ok(dtoPage);
    }

    @Operation(summary = "Get artist details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ArtistBasicResponseDTO> findById(@PathVariable Long id, HttpServletRequest request) {
        Artist artist = artistService.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        String sessionId = request.getSession().getId();
        visitService.registerArtistVisit(id, sessionId);

        return ResponseEntity.ok(ArtistBasicResponseDTO.fromModel(artist));
    }

    @Operation(summary = "Patch/update artist partially, optionally with new photo")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArtistBasicResponseDTO> patchArtist(
            @PathVariable Long id,
            @Valid @RequestPart("artist") ArtistPatchDTO dto,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        Artist updated = artistService.patchArtist(id, dto, photo);
        return ResponseEntity.ok(ArtistBasicResponseDTO.fromModel(updated));
    }

    @Operation(summary = "Delete artist by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        artistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
