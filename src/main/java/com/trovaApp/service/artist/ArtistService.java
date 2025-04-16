package com.trovaApp.service.artist;

import com.trovaApp.dto.artist.ArtistCreateDTO;
import com.trovaApp.dto.artist.ArtistPatchDTO;
import com.trovaApp.model.Artist;

import java.util.List;
import java.util.Optional;

public interface ArtistService {
    Artist create(ArtistCreateDTO dto);

    List<Artist> findAll();

    Optional<Artist> findById(Long id);

    Artist patchArtist(Long id, ArtistPatchDTO dto);

    void deleteById(Long id);
}