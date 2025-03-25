package com.trovaApp.service;

import com.trovaApp.model.Album;

import java.util.List;

public interface AlbumService {
    Album create(Album album);

    List<Album> findAll();
}
