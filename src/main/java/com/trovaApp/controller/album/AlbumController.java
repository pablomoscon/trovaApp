package com.trovaApp.controller.album;

import com.trovaApp.model.Album;
import com.trovaApp.service.album.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")

public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping
    public ResponseEntity<?> createAlbum(@RequestBody Album album) {

        return new ResponseEntity<>(
                albumService.create(album),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(albumService.findAll());
    }
}
