package com.trovaApp.controller.album;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trovaApp.dto.album.AlbumByIdResponseDTO;
import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.album.AlbumResponseDTO;
import com.trovaApp.dto.song.SongCreateDTO;
import com.trovaApp.dto.song.SongResponseDTO;
import com.trovaApp.enums.Genre;
import com.trovaApp.enums.Status;
import com.trovaApp.model.Album;
import com.trovaApp.service.album.AlbumService;
import com.trovaApp.service.visit.VisitService;
import com.trovaApp.util.AlbumUtils;
import com.trovaApp.util.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "albums", description = "Operations related to Albums")
@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final VisitService visitService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AlbumController(
            AlbumService albumService,
            VisitService visitService,
            ObjectMapper objectMapper
    ) {
        this.albumService = albumService;
        this.objectMapper = objectMapper;
        this.visitService = visitService;
    }

    @Operation(summary = "Create a new album with photo")
    @PostMapping
    public ResponseEntity<?> createAlbum(
            @RequestParam("album") String albumJson,
            @RequestPart("photo") MultipartFile photo
    ) throws JsonProcessingException {
        FileUtils.validateImageFile(photo);
        AlbumCreateDTO albumDTO = objectMapper.readValue(albumJson, AlbumCreateDTO.class);
        Album createdAlbum = albumService.create(albumDTO, photo);
        return new ResponseEntity<>(AlbumResponseDTO.fromModel(createdAlbum), HttpStatus.CREATED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = Map.of("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Map<String, String>> handleJsonProcessingException(JsonProcessingException ex) {
        Map<String, String> error = Map.of("error", "Invalid album JSON format");
        return ResponseEntity.badRequest().body(error);
    }

    @Operation(summary = "Get all albums paginated")
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Page<Album> albumPage = albumService.findAll(page, size);
        List<AlbumResponseDTO> dtoList = albumPage.getContent().stream()
                .map(AlbumResponseDTO::fromModel)
                .toList();

        return ResponseEntity.ok(AlbumUtils.buildPagedResponse("albums", albumPage, dtoList));
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> getAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) List<String> artistName,
            @RequestParam(required = false) List<Integer> year,
            @RequestParam(required = false) List<String> genre,
            @RequestParam(required = false, defaultValue = "") String sort
    ) {
        List<Genre> genreEnums = null;
        if (genre != null && !genre.isEmpty()) {
            genreEnums = genre.stream()
                    .map(String::toUpperCase)
                    .filter(g -> {
                        try {
                            Genre.valueOf(g);
                            return true;
                        } catch (IllegalArgumentException e) {
                            return false;
                        }
                    })
                    .map(Genre::valueOf)
                    .toList();
        }

        Page<Album> albumPage = albumService.findFiltered(page, size, artistName, year, genreEnums, sort);

        List<AlbumResponseDTO> dtoList = albumPage.getContent().stream()
                .map(AlbumResponseDTO::fromModel)
                .toList();

        return ResponseEntity.ok(AlbumUtils.buildPagedResponse("albums", albumPage, dtoList));
    }

    @Operation(summary = "Search albums by query text")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAlbums(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) Status status
    ) {
        Page<Album> albumPage = albumService.search(page, size, q, status);

        List<AlbumResponseDTO> dtoList = albumPage.getContent().stream()
                .map(AlbumResponseDTO::fromModel)
                .toList();

        return ResponseEntity.ok(AlbumUtils.buildPagedResponse("albums", albumPage, dtoList));
    }


    @Operation(summary = "Get albums by artist ID")
    @GetMapping("/by-artist/{artistId}")
    public ResponseEntity<Map<String, Object>> getAlbumsByArtist(
            @PathVariable Long artistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false, defaultValue = "") String sort
    ) {
        Page<Album> albumPage = albumService.findByArtistId(artistId, page, size, sort);

        List<AlbumResponseDTO> dtoList = albumPage.getContent().stream()
                .map(AlbumResponseDTO::fromModel)
                .toList();

        return ResponseEntity.ok(AlbumUtils.buildPagedResponse("albums", albumPage, dtoList));
    }

    @Operation(summary = "Add one or more songs to an album")
    @PostMapping("/{albumId}/add-songs")
    public ResponseEntity<List<SongResponseDTO>> addSongsToAlbum(
            @PathVariable Long albumId,
            @RequestBody List<SongCreateDTO> dtos) {

        List<SongResponseDTO> response = albumService.addSongsToAlbum(albumId, dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Get album details by ID, including songs")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id, HttpServletRequest request) {
        Album album = albumService.getAlbumWithSongs(id);

        if (album == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            visitService.registerAlbumVisit(id, session.getId());
        }

        return ResponseEntity.ok(AlbumByIdResponseDTO.fromModel(album));
    }

    @Operation(summary = "Patch/update album partially")
    @PatchMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> patch(
            @PathVariable Long id,
            @ModelAttribute AlbumPatchDTO dto) {
        Album updatedAlbum = albumService.patchAlbum(id, dto);
        return ResponseEntity.ok(AlbumResponseDTO.fromModel(updatedAlbum));
    }

    @Operation(summary = "Delete album by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        albumService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
