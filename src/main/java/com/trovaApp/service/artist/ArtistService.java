package com.trovaApp.service.artist;

import com.trovaApp.dto.artist.ArtistBasicResponseDTO;
import com.trovaApp.dto.artist.ArtistCreateDTO;
import com.trovaApp.dto.artist.ArtistPatchDTO;
import com.trovaApp.dto.artist.ArtistWithAlbumCountDTO;
import com.trovaApp.enums.Status;
import com.trovaApp.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ArtistService {
    Artist create(ArtistCreateDTO dto, MultipartFile photo);

    @Transactional(readOnly = true)
    Page<Artist> findAll(int page, int size, Status status);

    @Transactional(readOnly = true)
    Page<ArtistWithAlbumCountDTO> finArtistWithAlbumCount(int page, int size, Status status);

    Optional<Artist> findById(Long id);

    Artist patchArtist(Long id,
                       ArtistPatchDTO dto,
                       MultipartFile photo);

    void deleteById(Long id);

    Optional<Artist> findByName(String artistName);

    @Transactional(readOnly = true)
    Page<Artist> search(String term, int page, int size, Status status);

    @Transactional(readOnly = true)
    Page<Artist> findAllWithAlbums(Pageable pageable);

    long getTotalArtists();

    long getActiveArtist();

    long getSuspendedArtists();
}