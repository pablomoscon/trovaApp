package com.trovaApp.repository;

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

    @EntityGraph(attributePaths = {"listOfSongs", "genres", "artist"})
    Optional<Album> findWithDetailsById(Long id);

    @Query("SELECT DISTINCT a FROM Album a LEFT JOIN FETCH a.listOfSongs ls LEFT JOIN FETCH a.genres g LEFT JOIN FETCH a.artist")
    List<Album> findAllWithDetails();

    @Query("SELECT a.id FROM Album a")
    Page<Long> findAllAlbumIds(Pageable pageable);

    @Query("""
                SELECT DISTINCT a FROM Album a
                LEFT JOIN FETCH a.listOfSongs
                LEFT JOIN FETCH a.genres
                LEFT JOIN FETCH a.artist
                WHERE a.id IN :ids
            """)
    List<Album> findAllWithDetailsByIds(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"listOfSongs", "genres", "artist"})
    Page<Album> findByArtistId(Long artistId, Pageable pageable);
}

