package com.trovaApp.service.album;

import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.exception.AlbumNotFoundException;
import com.trovaApp.exception.ArtistNotFoundException;
import com.trovaApp.helper.S3Helper;
import com.trovaApp.helper.UserHelper;
import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Song;
import com.trovaApp.model.User;
import com.trovaApp.repository.AlbumRepository;
import com.trovaApp.service.activity.ActivityService;
import com.trovaApp.service.artist.ArtistService;
import com.trovaApp.service.song.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistService artistService;
    private final SongService songService;
    private final ActivityService activityService;
    private final UserHelper userHelper;
    private final S3Helper s3Helper;

    @Autowired
    public AlbumServiceImpl(AlbumRepository albumRepository,
                            ArtistService artistService,
                            SongService songService,
                            ActivityService activityService,
                            UserHelper userHelper,
                            S3Helper s3Helper
    ) {
        this.albumRepository = albumRepository;
        this.artistService = artistService;
        this.songService = songService;
        this.activityService = activityService;
        this.userHelper = userHelper;
        this.s3Helper = s3Helper;
    }

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

        String photoUrl = s3Helper.uploadPhoto(photo);
        album.setPhoto(photoUrl);

        List<Song> songs = songService.create(dto.getListOfSongs(), album, artist);
        album.setListOfSongs(songs);

        userHelper.logActivity("Created album: "
                + album.getArtist().getName()
                + " - " + album.getTitle());
        return albumRepository.save(album);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Album> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Long> albumIdsPage = albumRepository.findAllAlbumIds(pageable);

        List<Album> albums = albumRepository.findAllWithDetailsByIds(albumIdsPage.getContent());

        return new PageImpl<>(albums, pageable, albumIdsPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public Album getAlbumWithSongs(Long albumId) {
        return albumRepository.findWithDetailsById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));
    }

    @Transactional
    @Override
    public Album patchAlbum(Long id, AlbumPatchDTO dto) {
        Album album = albumRepository.findWithDetailsById(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        // Update album fields if provided
        Optional.ofNullable(dto.getTitle()).ifPresent(album::setTitle);
        Optional.ofNullable(dto.getDetails()).ifPresent(album::setDetails);
        Optional.ofNullable(dto.getCdNumber()).ifPresent(album::setCdNumber);
        Optional.ofNullable(dto.getPhoto()).ifPresent(album::setPhoto);
        Optional.ofNullable(dto.getYear()).ifPresent(album::setYear);
        Optional.ofNullable(dto.getGenres()).ifPresent(album::setGenres);
        Optional.ofNullable(dto.getDisplayArtistName()).ifPresent(album::setDisplayArtistName);
        Optional.ofNullable(dto.getStatus()).ifPresent(album::setStatus);

        // If artist ID is provided, update the artist
        if (dto.getArtistId() != null) {
            Artist artist = artistService.findById(dto.getArtistId())
                    .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));
            album.setArtist(artist);
        }

        // Log the user's activity
        userHelper.logActivity("Updated album: "
                + album.getArtist().getName()
                + " - " + album.getTitle());

        return albumRepository.save(album);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Album album = this.getAlbumWithSongs(id);
        if (album == null) {
            throw new AlbumNotFoundException("Album not found");
        }

        User user = userHelper.getAuthenticatedUser();
        activityService.logActivity(user, "Deleted album: "
                + album.getArtist().getName()
                + " - " + album.getTitle());

        albumRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Album> findByArtistId(Long artistId, Pageable pageable) {
        return albumRepository.findByArtistId(artistId, pageable);
    }
}
