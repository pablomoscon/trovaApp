package com.trovaApp.service.visit;

import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Visit;
import com.trovaApp.repository.AlbumRepository;
import com.trovaApp.repository.ArtistRepository;
import com.trovaApp.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class VisitServiceImpl implements VisitService {

    private static final Logger logger = LoggerFactory.getLogger(VisitServiceImpl.class);

    private final VisitRepository visitRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public VisitServiceImpl(VisitRepository visitRepository, AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.visitRepository = visitRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    /**
     * Registers a visit for a specific album.
     * Executed asynchronously in its own transaction.
     */
    @Async
    @Transactional
    @Override
    public void registerAlbumVisit(Long albumId, String sessionId) {
        if (visitRepository.existsBySessionIdAndAlbum_Id(sessionId, albumId)) {
            logger.debug("Album visit already exists for session {} and album {}", sessionId, albumId);
            return;
        }

        Optional<Album> albumOpt = albumRepository.findById(albumId);
        if (albumOpt.isEmpty()) {
            logger.warn("Album with ID {} not found. Visit not recorded.", albumId);
            return;
        }

        Visit visit = new Visit();
        visit.setSessionId(sessionId);
        visit.setVisitTime(LocalDateTime.now());
        visit.setAlbum(albumOpt.get());

        visitRepository.save(visit);
        logger.debug("Registered album visit: session={}, album={}", sessionId, albumId);
    }

    /**
     * Registers a visit for a specific artist.
     * Executed asynchronously in its own transaction.
     */
    @Async
    @Transactional
    @Override
    public void registerArtistVisit(Long artistId, String sessionId) {
        if (visitRepository.existsBySessionIdAndArtist_Id(sessionId, artistId)) {
            logger.debug("Artist visit already exists for session {} and artist {}", sessionId, artistId);
            return;
        }

        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        if (artistOpt.isEmpty()) {
            logger.warn("Artist with ID {} not found. Visit not recorded.", artistId);
            return;
        }

        Visit visit = new Visit();
        visit.setSessionId(sessionId);
        visit.setVisitTime(LocalDateTime.now());
        visit.setArtist(artistOpt.get());

        visitRepository.save(visit);
        logger.debug("Registered artist visit: session={}, artist={}", sessionId, artistId);
    }

    @Async
    @Override
    public CompletableFuture<Long> countAlbumVisits(Long albumId) {
        long count = visitRepository.countByAlbum_Id(albumId);
        return CompletableFuture.completedFuture(count);
    }

    @Async
    @Override
    public CompletableFuture<Long> countArtistVisits(Long artistId) {
        long count = visitRepository.countByArtist_Id(artistId);
        return CompletableFuture.completedFuture(count);
    }

    @Override
    public List<Map<String, Object>> getMostVisitedAlbums() {
        return visitRepository.findMostVisitedAlbumsWithTitle()
                .stream()
                .map(row -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("albumId", row[0]);
                    map.put("title", row[1]);
                    map.put("visits", row[2]);
                    return map;
                })
                .toList();
    }

    @Override
    public List<Map<String, Object>> getMostVisitedArtists() {
        return visitRepository.findMostVisitedArtistsWithName()
                .stream()
                .map(row -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("artistId", row[0]);
                    map.put("name", row[1]);
                    map.put("visits", row[2]);
                    return map;
                })
                .toList();
    }

    @Transactional
    @Override
    public void deleteByArtistId(Long artistId) {
        visitRepository.deleteByArtistId(artistId);
        logger.debug("Deleted all visits for artist {}", artistId);
    }
}
