package com.trovaApp.service.album;

import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.song.SongDTO;
import com.trovaApp.exception.AlbumNotFoundException;
import com.trovaApp.exception.ArtistNotFoundException;
import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Song;
import com.trovaApp.repository.AlbumRepository;
import com.trovaApp.service.artist.ArtistService;
import com.trovaApp.service.song.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistService artistService;
    private final SongService songService;

    @Autowired
    public AlbumServiceImpl(AlbumRepository albumRepository, ArtistService artistService, SongService songService) {
        this.albumRepository = albumRepository;
        this.artistService = artistService;
        this.songService = songService;
    }

    @Transactional
    @Override
    public Album create(AlbumCreateDTO albumCreateDTO) {
        Artist artist = artistService.findById(albumCreateDTO.getArtistId())
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        Album album = new Album();
        album.setPhoto(albumCreateDTO.getPhoto());
        album.setTitle(albumCreateDTO.getTitle());
        album.setDetails(albumCreateDTO.getDetails());
        album.setCdNumber(albumCreateDTO.getCdNumber());
        album.setYear(albumCreateDTO.getYear());
        album.setGenres(albumCreateDTO.getGenres());
        album.setArtist(artist);
        album.setDisplayArtistName(albumCreateDTO.getDisplayArtistName());

        List<SongDTO> songDTOs = albumCreateDTO.getListOfSongs();
        List<Song> songs = songService.create(songDTOs, album, artist);

        album.setListOfSongs(songs);

        return albumRepository.save(album);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Album> findAll() {
        return albumRepository.findAllWithDetails();
    }

    @Transactional(readOnly = true)
    @Override
    public Album getAlbumWithSongs(Long albumId) {
        Album album = albumRepository.findWithDetailsById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        Set<SongDTO> songDTOs = new HashSet<>();
        for (Song song : album.getListOfSongs()) {
            songDTOs.add(SongDTO.fromSong(song));
        }
        return album;
    }

    @Override
    @Transactional
    public Album patchAlbum(Long id, AlbumPatchDTO dto) {
        Album album = albumRepository.findWithDetailsById(id)
                .orElseThrow(() -> new AlbumNotFoundException("Album not found"));

        Optional.ofNullable(dto.getTitle()).ifPresent(album::setTitle);
        Optional.ofNullable(dto.getDetails()).ifPresent(album::setDetails);
        Optional.ofNullable(dto.getCdNumber()).ifPresent(album::setCdNumber);
        Optional.ofNullable(dto.getPhoto()).ifPresent(album::setPhoto);
        Optional.ofNullable(dto.getYear()).ifPresent(album::setYear);
        Optional.ofNullable(dto.getGenres()).ifPresent(album::setGenres);
        Optional.ofNullable(dto.getDisplayArtistName()).ifPresent(album::setDisplayArtistName);

        if (dto.getArtistId() != null) {
            Artist artist = artistService.findById(dto.getArtistId())
                    .orElseThrow(() -> new AlbumNotFoundException("Album not found"));
            album.setArtist(artist);
        }

        if (dto.getListOfSongs() != null) {
            List<Song> songs = songService.create(dto.getListOfSongs(), album, album.getArtist());
            album.setListOfSongs(songs);
        }

        return albumRepository.save(album);
    }

    @Override
    public void deleteById(Long id) {
        albumRepository.deleteById(id);
    }
}
