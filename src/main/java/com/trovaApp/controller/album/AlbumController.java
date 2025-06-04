package com.trovaApp.controller.album;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.album.AlbumResponseDTO;
import com.trovaApp.model.Album;
import com.trovaApp.service.album.AlbumService;
import com.trovaApp.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ObjectMapper objectMapper;

    @Autowired
    public AlbumController(
            AlbumService albumService,
            ObjectMapper objectMapper
    ) {

        this.albumService = albumService;
        this.objectMapper = objectMapper;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Album album = albumService.getAlbumWithSongs(id);
        return ResponseEntity.ok(AlbumResponseDTO.fromModel(album));
    }

    @GetMapping("/by-artist/{artistId}")
    public ResponseEntity<Map<String, Object>> getAlbumsByArtist(
            @PathVariable Long artistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
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
