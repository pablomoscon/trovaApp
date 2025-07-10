package com.trovaApp.repository;

import com.trovaApp.enums.Genre;
import com.trovaApp.enums.Status;
import com.trovaApp.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Fetch an album with songs, genres and artist eagerly loaded by ID
    @EntityGraph(attributePaths = {"listOfSongs", "genres", "artist"})
    Optional<Album> findWithDetailsById(Long id);

    // Fetch albums by artist ID with details eagerly loaded, paginated
    @EntityGraph(attributePaths = {"listOfSongs", "genres", "artist"})
    Page<Album> findByArtistId(Long artistId, Pageable pageable);

    // Fetch all albums by list of IDs with details eagerly loaded
    @EntityGraph(attributePaths = {"listOfSongs", "genres", "artist"})
    @Query("""
                SELECT a FROM Album a
                WHERE a.id IN :ids
            """)
    List<Album> findAllWithDetailsByIds(@Param("ids") List<Long> ids);

    // Fetch album IDs ordered by artist name ascending and album year descending, for pagination
    @Query("""
                SELECT a.id FROM Album a
                JOIN a.artist ar
                ORDER BY ar.name ASC, a.year DESC
            """)
    Page<Long> findAllAlbumIds(Pageable pageable);

    // Filter albums by optional artist names, years and genres
    @Query("""
                SELECT a.id FROM Album a
                JOIN a.artist ar
                WHERE (:artistNames IS NULL OR LOWER(ar.name) IN :artistNames)
                AND (:years IS NULL OR a.year IN :years)
                AND (:genres IS NULL OR EXISTS (
                    SELECT g FROM a.genres g WHERE g IN :genres
                ))
                ORDER BY ar.name ASC, a.year DESC
            """)
    Page<Long> findFilteredAlbumIds(
            @Param("artistNames") List<String> artistNames,
            @Param("years") List<Integer> years,
            @Param("genres") List<Genre> genres,
            Pageable pageable
    );

    @Query("""
        SELECT a.id
        FROM Album a
        JOIN a.artist ar
        WHERE LOWER(a.title)  LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(ar.name)  LIKE LOWER(CONCAT('%', :query, '%'))
        ORDER BY ar.name ASC, a.year DESC
       """)
    Page<Long> searchAlbumIds(@Param("query") String query, Pageable pageable);

    // Count albums by their status
    long countByStatus(Status status);
}
