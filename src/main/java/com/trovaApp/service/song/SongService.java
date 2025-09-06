package com.trovaApp.service.song;

import com.trovaApp.dto.song.SongAddToAlbumDTO;
import com.trovaApp.dto.song.SongCreateDTO;
import com.trovaApp.dto.song.SongPatchDTO;
import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Song;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SongService {
    Optional<Song> findById(Long id);

    @Transactional(readOnly = true)
    List<Song> findByAlbumId(Long albumId);

    List<Song> create(List<SongCreateDTO> songDTOs, Album album, Artist artist);

    List<Song> saveAll(List<Song> songs);

    Song patchSong(Long id, SongPatchDTO patchDTO);

    @Transactional
    void deleteByAlbumId(Long albumId);

    void deleteByIds(List<Long> ids);
}
