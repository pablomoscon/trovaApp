package com.trovaApp.service.artist;

import com.trovaApp.dto.artist.ArtistBasicResponseDTO;
import com.trovaApp.dto.artist.ArtistCreateDTO;
import com.trovaApp.dto.artist.ArtistPatchDTO;
import com.trovaApp.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ArtistService {
    Artist create(ArtistCreateDTO dto, MultipartFile photo);

    @Transactional(readOnly = true)
    Page<ArtistBasicResponseDTO> findAll(int page, int size);

    Optional<Artist> findById(Long id);

    Artist patchArtist(Long id, ArtistPatchDTO dto);

    void deleteById(Long id);
}