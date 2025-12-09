package com.trova_app.service.visit;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface VisitService {

    void registerAlbumVisit(Long albumId, String sessionId);

    void registerArtistVisit(Long artistId, String sessionId);

    @Async
    CompletableFuture<Long> countAlbumVisits(Long albumId);

    @Async
    CompletableFuture<Long> countArtistVisits(Long artistId);

    List<Map<String, Object>> getMostVisitedAlbums();

    List<Map<String, Object>> getMostVisitedArtists();

    @Transactional
    void deleteByArtistId(Long artistId);
}
