package com.trovaApp.service.song;

import com.trovaApp.dto.song.SongCreateDTO;
import com.trovaApp.dto.song.SongPatchDTO;
import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Song;

import java.util.List;
import java.util.Optional;

public interface SongService {
    Optional<Song> findById(Long id);

    List<Song> create(List<SongCreateDTO> songDTOs, Album album, Artist artist);

    Song patchSong(Long id, SongPatchDTO patchDTO);
}
