package com.trovaApp.repository;

import com.trovaApp.model.Song;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {

    @EntityGraph(attributePaths = {"artist"})
    List<Song> findByAlbumId(Long albumId);

        void deleteByAlbumId(Long albumId);

    }

