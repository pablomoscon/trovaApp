package com.trovaApp.controller.artist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trovaApp.dto.artist.*;
import com.trovaApp.exception.ArtistNotFoundException;
import com.trovaApp.model.Artist;
import com.trovaApp.service.artist.ArtistService;
import com.trovaApp.service.visit.VisitService;
import com.trovaApp.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


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

    @PostMapping
    public ResponseEntity<?> createArtist(@RequestParam("artist") String artistJson,
                                          @RequestPart("photo") MultipartFile photo
    ) throws JsonProcessingException {
        FileUtils.validateImageFile(photo);

        ArtistCreateDTO artistDTO = objectMapper.readValue(artistJson, ArtistCreateDTO.class);
        Artist artist = artistService.create(artistDTO, photo);
        return new ResponseEntity<>(ArtistBasicResponseDTO.fromModel(artist), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ArtistBasicResponseDTO>> getArtists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {

        Page<Artist> artistPage = artistService.findAll(page, size);

        Page<ArtistBasicResponseDTO> dtoPage = artistPage.map(ArtistBasicResponseDTO::fromModel);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ArtistBasicResponseDTO>> searchArtists(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {

        Page<Artist> artistPage = artistService.search(q, page, size);
        Page<ArtistBasicResponseDTO> dtoPage = artistPage.map(ArtistBasicResponseDTO::fromModel);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/with-albums")
    public ResponseEntity<Page<ArtistFullResponseDTO>> findAllWithAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Artist> artistsPage = artistService.findAllWithAlbums(pageable);

        List<ArtistFullResponseDTO> dtos = artistsPage.stream()
                .map(ArtistFullResponseDTO::from)
                .collect(Collectors.toList());

        Page<ArtistFullResponseDTO> dtoPage = new PageImpl<>(
                dtos,
                pageable,
                artistsPage.getTotalElements()
        );

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistBasicResponseDTO> findById(@PathVariable Long id, HttpServletRequest request) {
        Artist artist = artistService.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        String sessionId = request.getSession().getId();
        visitService.registerArtistVisit(id, sessionId);

        return ResponseEntity.ok(ArtistBasicResponseDTO.fromModel(artist));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArtistBasicResponseDTO> patchArtist(
            @PathVariable Long id,
            @Valid @RequestPart("artist") ArtistPatchDTO dto,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        Artist updated = artistService.patchArtist(id, dto, photo);
        return ResponseEntity.ok(ArtistBasicResponseDTO.fromModel(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        artistService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
