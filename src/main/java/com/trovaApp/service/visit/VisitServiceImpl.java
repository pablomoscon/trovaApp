package com.trovaApp.service.visit;

import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Visit;
import com.trovaApp.repository.AlbumRepository;
import com.trovaApp.repository.ArtistRepository;
import com.trovaApp.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    @Override
    public void registerAlbumVisit(Long albumId, String sessionId) {
        if (visitRepository.existsBySessionIdAndAlbum_Id(sessionId, albumId)) return;

        Album album = albumRepository.findById(albumId).orElse(null);
        if (album == null) return;

        Visit visit = new Visit();
        visit.setSessionId(sessionId);
        visit.setVisitTime(LocalDateTime.now());
        visit.setAlbum(album);

        visitRepository.save(visit);
    }

    @Override
    @Transactional
    public void registerArtistVisit(Long artistId, String sessionId) {
        if (visitRepository.existsBySessionIdAndArtist_Id(sessionId, artistId)) return;

        Artist artist = artistRepository.findById(artistId).orElse(null);
        if (artist == null) return;

        Visit visit = new Visit();
        visit.setSessionId(sessionId);
        visit.setVisitTime(LocalDateTime.now());
        visit.setArtist(artist);

        visitRepository.save(visit);
    }

    @Override
    public long countAlbumVisits(Long albumId) {
        return visitRepository.countByAlbum_Id(albumId);
    }

    @Override
    public long countArtistVisits(Long artistId) {
        return visitRepository.countByArtist_Id(artistId);
    }

    @Override
    public List<Map<String, Object>> getMostVisitedAlbums() {
        List<Object[]> raw = visitRepository.findMostVisitedAlbums();
        return raw.stream().map(row -> {
            Long albumId = (Long) row[0];
            Long count = (Long) row[1];
            Map<String, Object> map = new HashMap<>();
            map.put("albumId", albumId);
            map.put("title", albumRepository.findById(albumId).map(a -> a.getTitle()).orElse("Unknown"));
            map.put("visits", count);
            return map;
        }).toList();
    }

    @Override
    public List<Map<String, Object>> getMostVisitedArtists() {
        List<Object[]> raw = visitRepository.findMostVisitedArtists();
        return raw.stream().map(row -> {
            Long artistId = (Long) row[0];
            Long count = (Long) row[1];
            Map<String, Object> map = new HashMap<>();
            map.put("artistId", artistId);
            map.put("name", artistRepository.findById(artistId).map(a -> a.getName()).orElse("Unknown"));
            map.put("visits", count);
            return map;
        }).toList();
    }

    @Transactional
    @Override
    public void deleteByArtistId(Long artistId) {
        visitRepository.deleteByArtistId(artistId);
    }
}
