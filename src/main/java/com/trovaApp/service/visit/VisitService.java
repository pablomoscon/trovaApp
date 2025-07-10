package com.trovaApp.service.visit;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

public interface VisitService {

    @Transactional
    void registerAlbumVisit(Long albumId, String sessionId);

    void registerArtistVisit(Long artistId, String sessionId);

    long countAlbumVisits(Long albumId);

    long countArtistVisits(Long artistId);

    List<Map<String, Object>> getMostVisitedAlbums();

    List<Map<String, Object>> getMostVisitedArtists();

    @Transactional
    void deleteByArtistId(Long artistId);
}
