package com.trovaApp.service.song;

import com.trovaApp.dto.song.SongCreateDTO;
import com.trovaApp.dto.song.SongPatchDTO;
import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Song;
import com.trovaApp.repository.SongRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Autowired
    public SongServiceImpl(
            SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public Optional<Song> findById(Long id) {

        return songRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Song> findByAlbumId(Long albumId) {
        return songRepository.findByAlbumId(albumId);
    }

    @Override
    public List<Song> create(List<SongCreateDTO> songDTOs, Album album, Artist artist) {
        return songDTOs.stream().map(dto -> {
            Song song = new Song();
            song.setName(dto.getName());
            song.setDuration(dto.getDuration());
            song.setArtist(artist);
            song.setAlbum(album);
            return song;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Song> saveAll(List<Song> songs) {
        return songRepository.saveAll(songs);
    }


    @Override
    public Song patchSong(Long id, SongPatchDTO patchDTO) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found"));

        if (patchDTO.getName() != null) {
            song.setName(patchDTO.getName());
        }

        if (patchDTO.getDuration() != null) {
            song.setDuration(patchDTO.getDuration());
        }

        return songRepository.save(song);
    }

    @Transactional
    @Override
    public void deleteByAlbumId(Long albumId) {
        songRepository.deleteByAlbumId(albumId);
    }

    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        List<Song> foundSongs = songRepository.findAllById(ids);

        if (foundSongs.size() != ids.size()) {
            Set<Long> foundIds = foundSongs.stream()
                    .map(Song::getId)
                    .collect(Collectors.toSet());

            List<Long> notFoundIds = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new EntityNotFoundException("Songs with ids " + notFoundIds + " not found");
        }

        songRepository.deleteAll(foundSongs);
    }
}
