package com.trova_app.service.album;

import com.trova_app.dto.album.AlbumCreateDTO;
import com.trova_app.dto.album.AlbumPatchDTO;
import com.trova_app.dto.song.SongCreateDTO;
import com.trova_app.dto.song.SongResponseDTO;
import com.trova_app.enums.Genre;
import com.trova_app.enums.Status;
import com.trova_app.exception.AlbumNotFoundException;
import com.trova_app.exception.ArtistNotFoundException;
import com.trova_app.helper.S3Helper;
import com.trova_app.helper.UserHelper;
import com.trova_app.model.Album;
import com.trova_app.model.Artist;
import com.trova_app.model.Song;
import com.trova_app.model.User;
import com.trova_app.repository.AlbumRepository;
import com.trova_app.service.activity.ActivityService;
import com.trova_app.service.artist.ArtistService;
import com.trova_app.service.song.SongService;
import com.trova_app.service.visit.VisitService;
import com.trova_app.util.AlbumUtils;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistService artistService;
    private final SongService songService;
    private final ActivityService activityService;
    private final UserHelper userHelper;
    private final S3Helper s3Helper;
    private final VisitService visitService;


    @Autowired
    public AlbumServiceImpl(
            AlbumRepository albumRepository,
            ArtistService artistService,
            SongService songService,
            ActivityService activityService,
            UserHelper userHelper,
            S3Helper s3Helper,
            VisitService visitService
    ) {
        this.albumRepository = albumRepository;
        this.artistService = artistService;
        this.songService = songService;
        this.activityService = activityService;
        this.userHelper = userHelper;
        this.s3Helper = s3Helper;
        this.visitService = visitService;
    }

    private static final String ALBUM_NOT_FOUND_MESSAGE = "Album not found";

    // Create new album
    @Transactional
    @Override
    public Album create(AlbumCreateDTO dto, MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("Photo is required to create an album");
        }

        Artist artist = artistService.findById(dto.getArtistId())
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        Album album = new Album();
        album.setTitle(dto.getTitle());
        album.setDetails(dto.getDetails());
        album.setCdNumber(dto.getCdNumber());
        album.setYear(dto.getYear());
        album.setGenres(dto.getGenres());
        album.setArtist(artist);
        album.setDisplayArtistName(dto.getDisplayArtistName());
        album.setAmazonMusicLink(dto.getAmazonMusicLink());
        album.setAppleMusicLink(dto.getAppleMusicLink());
        album.setSpotifyLink(dto.getSpotifyLink());

        String photoUrl = s3Helper.uploadPhoto(photo);
        album.setPhoto(photoUrl);

        List<Song> songs = songService.create(dto.getListOfSongs(), album, artist);
        album.setListOfSongs(songs);

        userHelper.logActivity("Created album: " + artist.getName() + " - " + album.getTitle());
        return albumRepository.save(album);
    }

    // Update existing album
    @Transactional
    @Override
    public Album patchAlbum(Long id, AlbumPatchDTO dto) {
        Album album = albumRepository.findWithDetailsById(id)
                .orElseThrow(() -> new AlbumNotFoundException(ALBUM_NOT_FOUND_MESSAGE));

        Optional.ofNullable(dto.getTitle()).ifPresent(album::setTitle);
        Optional.ofNullable(dto.getDetails()).ifPresent(album::setDetails);
        Optional.ofNullable(dto.getCdNumber()).ifPresent(album::setCdNumber);
        Optional.ofNullable(dto.getYear()).ifPresent(album::setYear);
        Optional.ofNullable(dto.getGenres()).ifPresent(album::setGenres);
        Optional.ofNullable(dto.getDisplayArtistName()).ifPresent(album::setDisplayArtistName);
        Optional.ofNullable(dto.getStatus()).ifPresent(album::setStatus);
        Optional.ofNullable(dto.getAmazonMusicLink()).ifPresent(album::setAmazonMusicLink);
        Optional.ofNullable(dto.getSpotifyLink()).ifPresent(album::setSpotifyLink);
        Optional.ofNullable(dto.getAppleMusicLink()).ifPresent(album::setAppleMusicLink);

        if (dto.getArtistId() != null) {
            Artist artist = artistService.findById(dto.getArtistId())
                    .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));
            album.setArtist(artist);
        }

        MultipartFile photoFile = dto.getPhoto();
        if (photoFile != null && !photoFile.isEmpty()) {
            String photoUrl = s3Helper.uploadPhoto(photoFile);
            album.setPhoto(photoUrl);
        }

        userHelper.logActivity("Updated album: " + album.getArtist().getName() + " - " + album.getTitle());
        return albumRepository.save(album);
    }

// Add one or more songs to an album

    @Transactional
    @Override
    public List<SongResponseDTO> addSongsToAlbum(Long albumId, List<SongCreateDTO> dtos) {
        Album album = this.findById(albumId);

        if (album.getArtist() == null) {
            throw new IllegalStateException("Album has no associated artist");
        }

        List<Song> songs = dtos.stream().map(dto -> {
            Song song = new Song();
            song.setName(dto.getName());
            song.setDuration(dto.getDuration());
            song.setAlbum(album);
            song.setArtist(album.getArtist());
            return song;
        }).toList();

        List<Song> savedSongs = songService.saveAll(songs);

        return savedSongs.stream()
                .map(SongResponseDTO::fromModel)
                .toList();
    }

    // Delete album by ID
    @Transactional
    @Override
    public void deleteById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException(ALBUM_NOT_FOUND_MESSAGE));

        // Delete songs associated with this album
        songService.deleteByAlbumId(id);

        User user = userHelper.getAuthenticatedUser();
        activityService.logActivity(
                user,
                "Deleted album: " + album.getArtist().getName() + " - " + album.getTitle()
        );
        albumRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Album saveRaw(Album album) {
        return albumRepository.save(album);
    }

    // Remove songs from Album

    public void removeSongsFromAlbum(Long albumId, List<Long> songIdsToRemove) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException(ALBUM_NOT_FOUND_MESSAGE));

        List<Song> updatedSongs = album.getListOfSongs().stream()
                .filter(song -> !songIdsToRemove.contains(song.getId()))
                .collect(Collectors.toList());

        album.setListOfSongs(updatedSongs);
        albumRepository.save(album);
    }

    // Get album by ID
    @Override
    public Album findById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + id + " not found"));
    }

    // Get album with Details by ID
    @Transactional
    @Override
    public Album findWithDetailsById(Long id, boolean registerVisit, HttpSession session) {
        Album album = albumRepository.findWithDetailsById(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album with id " + id + " not found"));

        if (registerVisit && session != null) {
            visitService.registerAlbumVisit(id, session.getId());
        }

        return album;
    }

    // Get all albums paginated
    @Transactional(readOnly = true)
    @Override
    public Page<Album> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Long> albumIdsPage = albumRepository.findAllAlbumIds(pageable);

        List<Album> albums = albumRepository.findAllWithDetailsByIds(albumIdsPage.getContent());

        return new PageImpl<>(albums, pageable, albumIdsPage.getTotalElements());
    }

    // Get albums by artist
    @Transactional(readOnly = true)
    @Override
    public Page<Album> findByArtistId(Long artistId, int page, int size, String sortOrder) {
        Pageable pageable = AlbumUtils.buildPageRequest(page, size, sortOrder);
        Page<Album> albumsPage = albumRepository.findByArtistIdAndStatus(artistId, Status.ACTIVE, pageable);

        List<Album> ordered = AlbumUtils.orderByTitle(albumsPage.getContent());

        return new PageImpl<>(ordered, pageable, albumsPage.getTotalElements());
    }

    // Get the total number of albums for a given artist by ID
    @Transactional(readOnly = true)
    @Override
    public Long countAlbumsByArtist(Long artistId) {
        return albumRepository.countByArtistId(artistId);
    }

    // Get albums with filters
    @Transactional(readOnly = true)
    @Override
    public Page<Album> findFiltered(
            int page,
            int size,
            List<String> artistNames,
            List<Integer> years,
            List<Genre> genres,
            String sortOrder
    ) {
        Pageable pageable = AlbumUtils.buildPageRequest(page, size, sortOrder);

        List<String> normalizedArtists = AlbumUtils.normalizeArtistNames(artistNames);
        if (normalizedArtists != null && normalizedArtists.isEmpty()) normalizedArtists = null;
        if (years != null && years.isEmpty()) years = null;
        if (genres != null && genres.isEmpty()) genres = null;

        Page<Long> albumIdsPage = albumRepository.findFilteredAlbumIds(normalizedArtists, years, genres, pageable);
        if (albumIdsPage.isEmpty()) return Page.empty(pageable);

        List<Long> ids = albumIdsPage.getContent();

        List<Album> albums = albumRepository.findAllWithDetailsByIds(ids);

        List<Album> ordered = AlbumUtils.orderByIds(ids, albums, Album::getId);

        return new PageImpl<>(ordered, pageable, albumIdsPage.getTotalElements());
    }


    // Search albums
    @Transactional(readOnly = true)
    @Override
    public Page<Album> search(int page, int size, String query, @Nullable Status status) {
        if (query == null || query.trim().isEmpty()) {
            return Page.empty(PageRequest.of(page, size));
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Long> albumIdsPage = albumRepository.searchAlbumIds(query.trim(), status, pageable);

        if (albumIdsPage.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Album> albums = albumRepository.findAllWithDetailsByIds(albumIdsPage.getContent());
        List<Album> ordered = AlbumUtils.orderByTitle(albums);

        return new PageImpl<>(ordered, pageable, albumIdsPage.getTotalElements());
    }

    // Get available filters: artists, genres, decades
    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> getAvailableFilters() {
        List<String> artists = albumRepository.findAllArtistsEntity()
                .stream()
                .map(Artist::getName)
                .filter(Objects::nonNull)
                .sorted()
                .toList();
        List<String> genres = albumRepository.findAllGenres()
                .stream()
                .map(Enum::name)
                .toList();
        List<String> decades = albumRepository.findAllYears()
                .stream()
                .map(y -> (y / 10) * 10 + "s")
                .distinct()
                .sorted((a, b) -> b.compareTo(a)) // descending
                .toList();

        Map<String, Object> result = new HashMap<>();
        result.put("artists", artists);
        result.put("genres", genres);
        result.put("decades", decades);

        return result;
    }

    // Stats
    @Override
    public long getTotalAlbums() {
        return albumRepository.count();
    }

    @Override
    public long getActiveAlbums() {
        return albumRepository.countByStatus(Status.ACTIVE);
    }

    @Override
    public long getSuspendedAlbums() {
        return albumRepository.countByStatus(Status.SUSPENDED);
    }
}
