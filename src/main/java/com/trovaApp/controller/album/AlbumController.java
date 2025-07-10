package com.trovaApp.controller.album;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trovaApp.dto.album.AlbumByIdResponseDTO;
import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.album.AlbumResponseDTO;
import com.trovaApp.enums.Genre;
import com.trovaApp.model.Album;
import com.trovaApp.service.album.AlbumService;
import com.trovaApp.service.visit.VisitService;
import com.trovaApp.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Page<Album> albumPage = albumService.findAll(page, size);

        List<AlbumResponseDTO> dtoList = albumPage.getContent().stream()
                .map(AlbumResponseDTO::fromModel)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("albums", dtoList);
        response.put("currentPage", albumPage.getNumber());
        response.put("totalItems", albumPage.getTotalElements());
        response.put("totalPages", albumPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> getAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) List<String> artistName,
            @RequestParam(required = false) List<Integer> year,
            @RequestParam(required = false) List<String> genre
    ) {
        List<Genre> genreEnums = null;
        if (genre != null && !genre.isEmpty()) {
            genreEnums = genre.stream()
                    .map(g -> Genre.valueOf(g.toUpperCase()))
                    .toList();
        }

        Page<Album> albumPage = albumService.findFiltered(page, size, artistName, year, genreEnums);

        List<AlbumResponseDTO> dtoList = albumPage.getContent().stream()
                .map(AlbumResponseDTO::fromModel)
                .toList();

        Map<String, Object> response = Map.of(
                "albums", dtoList,
                "currentPage", albumPage.getNumber(),
                "totalItems", albumPage.getTotalElements(),
                "totalPages", albumPage.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAlbums(
            @RequestParam String q,                     // texto a buscar
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Page<Album> albumPage = albumService.search(page, size, q);

        List<AlbumResponseDTO> dtoList = albumPage.getContent().stream()
                .map(AlbumResponseDTO::fromModel)
                .toList();

        Map<String, Object> response = Map.of(
                "albums", dtoList,
                "currentPage", albumPage.getNumber(),
                "totalItems", albumPage.getTotalElements(),
                "totalPages", albumPage.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-artist/{artistId}")
    public ResponseEntity<Map<String, Object>> getAlbumsByArtist(
            @PathVariable Long artistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        // order by title ascending, case‑insensitive
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Order.asc("title").ignoreCase())
        );

        Page<Album> albumPage = albumService.findByArtistId(artistId, pageable);

        List<AlbumResponseDTO> dtoList = albumPage.getContent().stream()
                .map(AlbumResponseDTO::fromModel)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("albums", dtoList);
        response.put("currentPage", albumPage.getNumber());
        response.put("totalItems", albumPage.getTotalElements());
        response.put("totalPages", albumPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id, HttpServletRequest request) {
        Album album = albumService.getAlbumWithSongs(id);

        String sessionId = request.getSession().getId();
        visitService.registerAlbumVisit(id, sessionId);

        return ResponseEntity.ok(AlbumByIdResponseDTO.fromModel(album));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> patch(
            @PathVariable Long id,
            @ModelAttribute AlbumPatchDTO dto) {
        Album updatedAlbum = albumService.patchAlbum(id, dto);
        return ResponseEntity.ok(AlbumResponseDTO.fromModel(updatedAlbum));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        albumService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
