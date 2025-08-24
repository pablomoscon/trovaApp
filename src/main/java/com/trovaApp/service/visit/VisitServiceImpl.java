package com.trovaApp.service.visit;

import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Visit;
import com.trovaApp.repository.AlbumRepository;
import com.trovaApp.repository.ArtistRepository;
import com.trovaApp.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public VisitServiceImpl(VisitRepository visitRepository, AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.visitRepository = visitRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    @Async
    public void registerAlbumVisit(Long albumId, String sessionId) {
        if (visitRepository.existsBySessionIdAndAlbum_Id(sessionId, albumId)) return;

        Album album = albumRepository.getReferenceById(albumId);

        Visit visit = new Visit();
        visit.setSessionId(sessionId);
        visit.setVisitTime(LocalDateTime.now());
        visit.setAlbum(album);

        visitRepository.save(visit);
    }

    @Override
    @Async
    public void registerArtistVisit(Long artistId, String sessionId) {
        if (visitRepository.existsBySessionIdAndArtist_Id(sessionId, artistId)) return;

        Artist artist = artistRepository.getReferenceById(artistId);

        Visit visit = new Visit();
        visit.setSessionId(sessionId);
        visit.setVisitTime(LocalDateTime.now());
        visit.setArtist(artist);

        visitRepository.save(visit);
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
    }
}
