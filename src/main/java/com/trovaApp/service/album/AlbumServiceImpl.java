package com.trovaApp.service.album;

import com.trovaApp.model.Album;
import com.trovaApp.repository.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumServiceImpl (AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public Album create(Album album) {

        album.setCreatedAt(new Date());

       return albumRepository.save(album);
    }

    @Override
    public List<Album> findAll(){

        return albumRepository.findAll();
    }
}
