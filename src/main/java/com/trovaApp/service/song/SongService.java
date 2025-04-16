package com.trovaApp.service.song;

import com.trovaApp.dto.song.SongDTO;
import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Song;

import java.util.List;
import java.util.Set;

public interface SongService {
    List<Song> create(List<SongDTO> songDTOs, Album album, Artist artist);
}
