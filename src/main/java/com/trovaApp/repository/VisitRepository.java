package com.trovaApp.repository;

import com.trovaApp.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    boolean existsBySessionIdAndAlbum_Id(String sessionId, Long albumId);

    boolean existsBySessionIdAndArtist_Id(String sessionId,Long artistId);

    long countByAlbum_Id(Long albumId);

    long countByArtist_Id(Long artistId);

    @Query("SELECT v.album.id, COUNT(v) FROM Visit v WHERE v.album IS NOT NULL GROUP BY v.album.id ORDER BY COUNT(v) DESC")
    List<Object[]> findMostVisitedAlbums();

    @Query("SELECT v.artist.id, COUNT(v) FROM Visit v WHERE v.artist IS NOT NULL GROUP BY v.artist.id ORDER BY COUNT(v) DESC")
    List<Object[]> findMostVisitedArtists();

    @Transactional
    @Modifying
    @Query("DELETE FROM Visit v WHERE v.artist.id = :artistId")
    void deleteByArtistId(@Param("artistId") Long artistId);

}
