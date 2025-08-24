package com.trovaApp.repository;

import com.trovaApp.dto.artist.ArtistWithAlbumCountDTO;
import com.trovaApp.enums.Status;
import com.trovaApp.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    long countByStatus(Status status);

    Optional<Artist> findByName(String name);

    @Query("""
           SELECT a
           FROM   Artist a
           WHERE  LOWER(a.name)        LIKE LOWER(CONCAT('%', :term, '%'))
              OR  LOWER(a.nationality) LIKE LOWER(CONCAT('%', :term, '%'))
           """)
    Page<Artist> searchByTerm(@Param("term") String term, Pageable pageable);

    @EntityGraph(attributePaths = {"albums", "albums.genres", "albums.listOfSongs"})
    Page<Artist> findAll(Pageable pageable);

    @Query("""
       SELECT a
       FROM Artist a
       WHERE (LOWER(a.name) LIKE LOWER(CONCAT('%', :term, '%'))
           OR LOWER(a.nationality) LIKE LOWER(CONCAT('%', :term, '%')))
         AND a.status = :status
       """)
    Page<Artist> searchByTermAndStatus(@Param("term") String term, @Param("status") Status status, Pageable pageable);

    @Query("SELECT new com.trovaApp.dto.artist.ArtistWithAlbumCountDTO(" +
            "a.id, a.name, a.details, a.nationality, a.photo, a.createdAt, a.status, COUNT(alb)) " +
            "FROM Artist a LEFT JOIN a.albums alb " +
            "WHERE (:status IS NULL OR a.status = :status) " +
            "GROUP BY a.id, a.name, a.details, a.nationality, a.photo, a.createdAt, a.status")
    Page<ArtistWithAlbumCountDTO> findAllWithAlbumCount(@Param("status") Status status, Pageable pageable);

    Page<Artist> findByStatus(Status status, Pageable pageable);
    }


