package com.trovaApp.repository;
import com.trovaApp.model.Album;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository <Album, Long> {

    @EntityGraph(attributePaths = {"listOfSongs", "genres", "artist"})
    Optional<Album> findWithDetailsById(Long id);

    @Query("SELECT DISTINCT a FROM Album a LEFT JOIN FETCH a.listOfSongs ls LEFT JOIN FETCH a.genres g LEFT JOIN FETCH a.artist")
    List<Album> findAllWithDetails();

}
