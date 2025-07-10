package com.trovaApp.repository;

import com.trovaApp.enums.Status;
import com.trovaApp.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
}