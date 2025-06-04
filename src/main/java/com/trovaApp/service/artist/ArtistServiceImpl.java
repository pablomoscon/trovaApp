package com.trovaApp.service.artist;

import com.trovaApp.dto.artist.ArtistBasicResponseDTO;
import com.trovaApp.dto.artist.ArtistCreateDTO;
import com.trovaApp.dto.artist.ArtistPatchDTO;
import com.trovaApp.exception.ArtistNotFoundException;
import com.trovaApp.helper.S3Helper;
import com.trovaApp.helper.UserHelper;
import com.trovaApp.model.Artist;
import com.trovaApp.repository.ArtistRepository;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final UserHelper userHelper;
    private final S3Helper s3Helper;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository,
                             UserHelper userHelper,
                             S3Helper s3Helper
    ) {
        this.artistRepository = artistRepository;
        this.userHelper = userHelper;
        this.s3Helper = s3Helper;
    }

    @Override
    public Artist create(ArtistCreateDTO dto, MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("Photo is required to create an artist");
        }
        Artist artist = new Artist();
        artist.setName(dto.getName());
        artist.setDetails(dto.getDetails());
        artist.setNationality(dto.getNationality());

        String photoUrl = s3Helper.uploadPhoto(photo);
        artist.setPhoto(photoUrl);

        userHelper.logActivity("Created Artist: " + artist.getName());

        return artistRepository.save(artist);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ArtistBasicResponseDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Artist> artists = artistRepository.findAll(pageable);
        return artists.map(ArtistBasicResponseDTO::from);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Artist> findById(Long id) {
        return artistRepository.findById(id);
    }

    @Override
    public Artist patchArtist(Long id, ArtistPatchDTO dto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        if (dto.getName() != null) artist.setName(dto.getName());
        if (dto.getDetails() != null) artist.setDetails(dto.getDetails());
        if (dto.getNationality() != null) artist.setNationality(dto.getNationality());
        if (dto.getPhoto() != null) artist.setPhoto(dto.getPhoto());
        if (dto.getStatus() != null) artist.setStatus(dto.getStatus());

        userHelper.logActivity("Updated artist: " + artist.getName());

        return artistRepository.save(artist);
    }

    @Override
    public void deleteById(Long id) {

        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        userHelper.logActivity("Delete artist: " + artist.getName());

        artistRepository.deleteById(id);
    }
}
