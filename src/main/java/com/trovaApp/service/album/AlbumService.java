package com.trovaApp.service.album;

import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.song.SongCreateDTO;
import com.trovaApp.dto.song.SongResponseDTO;
import com.trovaApp.enums.Genre;
import com.trovaApp.enums.Status;
import com.trovaApp.model.Album;
import com.trovaApp.model.Song;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AlbumService {

    // Create

    Album create(AlbumCreateDTO dto, MultipartFile photo);

    // Read / Get

   Album findById(Long id);


 // Get album with Details by ID
 @Transactional(readOnly = true)
 Album findWithDetailsById(Long id);

 Page<Album> findAll(int page, int size);

    // Filtering and Search

    // Get albums by artist


    // Get albums by artist
    @Transactional(readOnly = true)
    Page<Album> findByArtistId(Long artistId, int page, int size, String sortOrder);

    // Get the total number of albums for a given artist by ID
    @Transactional(readOnly = true)
    Long countAlbumsByArtist(Long artistId);

    Page<Album> findFiltered(
            int page,
            int size,
            List<String> artistNames,
            List<Integer> years,
            List<Genre> genres,
            String sortOrder
    );

    // Update / Patch

    Album patchAlbum(Long id, AlbumPatchDTO dto);

    Album saveRaw(Album album);

    //  Delete

    void deleteById(Long id);

    // Stats / Utilities

    // Search albums
    @Transactional(readOnly = true)
    Page<Album> search(int page, int size, String query, @Nullable Status status);

    @Transactional(readOnly = true)
    Map<String, Object> getAvailableFilters();

    long getTotalAlbums();

    long getActiveAlbums();

    long getSuspendedAlbums();

    @Transactional
    List<SongResponseDTO> addSongsToAlbum(Long albumId, List<SongCreateDTO> dtos);
}
