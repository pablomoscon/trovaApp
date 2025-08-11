package com.trovaApp.service.album;

import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.song.SongCreateDTO;
import com.trovaApp.dto.song.SongResponseDTO;
import com.trovaApp.enums.Genre;
import com.trovaApp.model.Album;
import com.trovaApp.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface AlbumService {

    // Create

    Album create(AlbumCreateDTO dto, MultipartFile photo);

    // Read / Get

    Optional<Album> findById(Long id);

    Album getAlbumWithSongs(Long albumId);

    Page<Album> findAll(int page, int size);

    Page<Album> findByArtistId(Long artistId, int page, int size, String sortOrder);

    // Filtering and Search

    Page<Album> findFiltered(
            int page,
            int size,
            List<String> artistNames,
            List<Integer> years,
            List<Genre> genres,
            String sortOrder
    );

    Page<Album> search(int page, int size, String query);

    // Update / Patch

    Album patchAlbum(Long id, AlbumPatchDTO dto);

    Album saveRaw(Album album);

    //  Delete

    void deleteById(Long id);

    // Stats / Utilities

    long getTotalAlbums();

    long getActiveAlbums();

    long getSuspendedAlbums();

    @Transactional
    List<SongResponseDTO> addSongsToAlbum(Long albumId, List<SongCreateDTO> dtos);
}
