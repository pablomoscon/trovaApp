package com.trovaApp.service.album;

import com.trovaApp.dto.album.AlbumCreateDTO;
import com.trovaApp.dto.album.AlbumPatchDTO;
import com.trovaApp.dto.album.AlbumResponseDTO;
import com.trovaApp.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AlbumService {

    Album create(AlbumCreateDTO dto, MultipartFile photo);

    Page<Album> findAll(int page, int size);

    Album getAlbumWithSongs(Long albumId);

    Album patchAlbum(Long id, AlbumPatchDTO dto);

    void deleteById(Long id);

    @Transactional(readOnly = true)
    Page<Album> findByArtistId(Long artistId, Pageable pageable);
}
