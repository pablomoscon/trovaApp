package com.trovaApp.service.album;

import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.album.AlbumResponseDTO;
import com.trovaApp.enums.Genre;
import com.trovaApp.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlbumService {

    Album create(AlbumCreateDTO dto, MultipartFile photo);

    Page<Album> findAll(int page, int size);

    @Transactional(readOnly = true)
    Optional<Album> findById(Long id);

    Album getAlbumWithSongs(Long albumId);

    @Transactional(readOnly = true)
    Page<Album> findFiltered(
            int page,
            int size,
            List<String> artistNames,
            List<Integer> years,
            List<Genre> genres
    );

    Album patchAlbum(Long id, AlbumPatchDTO dto);

    void deleteById(Long id);

    @Transactional
    Album saveRaw(Album album);

    @Transactional(readOnly = true)
    Page<Album> findByArtistId(Long artistId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<Album> search(int page, int size, String query);

    long getTotalAlbums();

    long getActiveAlbums();

    long getSuspendedAlbums();
}
