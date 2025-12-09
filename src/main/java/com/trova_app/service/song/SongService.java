package com.trova_app.service.song;

import com.trova_app.dto.song.SongAddToAlbumDTO;
import com.trova_app.dto.song.SongCreateDTO;
import com.trova_app.dto.song.SongPatchDTO;
import com.trova_app.model.Album;
import com.trova_app.model.Artist;
import com.trova_app.model.Song;
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
