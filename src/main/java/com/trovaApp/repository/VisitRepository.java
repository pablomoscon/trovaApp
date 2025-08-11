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

    @Query("""
    SELECT a.id, a.title, COUNT(v)
    FROM Visit v
    JOIN v.album a
    GROUP BY a.id, a.title
    ORDER BY COUNT(v) DESC
""")
    List<Object[]> findMostVisitedAlbumsWithTitle();

    @Query("""
    SELECT ar.id, ar.name, COUNT(v)
    FROM Visit v
    JOIN v.artist ar
    GROUP BY ar.id, ar.name
    ORDER BY COUNT(v) DESC
""")
    List<Object[]> findMostVisitedArtistsWithName();

    @Transactional
    @Modifying
    @Query("DELETE FROM Visit v WHERE v.artist.id = :artistId")
    void deleteByArtistId(@Param("artistId") Long artistId);

}
