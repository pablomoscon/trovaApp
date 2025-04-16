package com.trovaApp.service.album;

import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.album.AlbumResponseDTO;
import com.trovaApp.model.Album;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AlbumService {

    Album create(AlbumCreateDTO dto);

    List<Album> findAll();

    Album getAlbumWithSongs(Long albumId);

    Album patchAlbum(Long id, AlbumPatchDTO dto);

    void deleteById(Long id);
}
