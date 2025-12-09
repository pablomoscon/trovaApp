package com.trova_app.service.album;

import com.trova_app.dto.album.AlbumCreateDTO;
import com.trova_app.dto.album.AlbumPatchDTO;
import com.trova_app.dto.song.SongCreateDTO;
import com.trova_app.dto.song.SongResponseDTO;
import com.trova_app.enums.Genre;
import com.trova_app.enums.Status;
import com.trova_app.model.Album;
import com.trova_app.model.Song;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AlbumService {

    // Create a new album with optional photo
    Album create(AlbumCreateDTO dto, MultipartFile photo);

    // Get album by ID (basic info)
    Album findById(Long id);

    // Get album with artist and genres (optionally register a visit)
    Album findWithDetailsById(Long id, boolean registerVisit, HttpSession session);

    // Get all albums, paginated and sorted
    Page<Album> findAll(int page, int size);

    // Get albums by artist ID, with pagination and sorting
    Page<Album> findByArtistId(Long artistId, int page, int size, String sortOrder);

    // Count how many albums belong to a given artist
    Long countAlbumsByArtist(Long artistId);

    // Filter albums by artists, years and genres
    Page<Album> findFiltered(
            int page,
            int size,
            List<String> artistNames,
            List<Integer> years,
            List<Genre> genres,
            String sortOrder
    );

    // Search albums by title or artist name
    Page<Album> search(int page, int size, String query, @Nullable Status status);

    // Update album information partially
    Album patchAlbum(Long id, AlbumPatchDTO dto);

    // Save raw album object (used internally)
    Album saveRaw(Album album);

    // Delete album by ID
    void deleteById(Long id);

    // Get available filters (artists, years, genres)
    Map<String, Object> getAvailableFilters();

    // Get album counts (total / by status)
    long getTotalAlbums();

    long getActiveAlbums();

    long getSuspendedAlbums();

    // Add new songs to an album
    List<SongResponseDTO> addSongsToAlbum(Long albumId, List<SongCreateDTO> dtos);
}
