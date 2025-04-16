package com.trovaApp.service.song;

import com.trovaApp.dto.song.SongDTO;
import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Song;
import com.trovaApp.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Autowired
    public SongServiceImpl(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override
    public List<Song> create(List<SongDTO> songDTOs, Album album, Artist artist) {
        List<Song> songs = new ArrayList<>();
        for (SongDTO songDTO : songDTOs) {
            Song song = new Song();
            song.setName(songDTO.getName());
            song.setDuration(songDTO.getDuration());
            song.setAlbum(album);
            song.setArtist(artist);
            song = songRepository.save(song);
            songs.add(song);
        }
        return songs;
    }
}