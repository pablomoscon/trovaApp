package com.trovaApp.repository;

import com.trovaApp.model.Artist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @EntityGraph(attributePaths = {"albums", "albums.listOfSongs", "albums.genres"})
    Optional<Artist> findWithAlbumsById(Long id);
}