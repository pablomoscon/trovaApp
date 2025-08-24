package com.trovaApp.service.artist;

import com.trovaApp.dto.artist.ArtistCreateDTO;
import com.trovaApp.dto.artist.ArtistPatchDTO;
import com.trovaApp.dto.artist.ArtistWithAlbumCountDTO;
import com.trovaApp.enums.Status;
import com.trovaApp.exception.ArtistNotFoundException;
import com.trovaApp.helper.S3Helper;
import com.trovaApp.helper.UserHelper;
import com.trovaApp.model.Artist;
import com.trovaApp.repository.ArtistRepository;
import com.trovaApp.service.visit.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final VisitService visitService;
    private final UserHelper userHelper;
    private final S3Helper s3Helper;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository,
                             VisitService visitService,
                             UserHelper userHelper,
                             S3Helper s3Helper
    ) {
        this.artistRepository = artistRepository;
        this.visitService = visitService;
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
    public Page<Artist> findAll(int page, int size, Status status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));

        if (status != null) {
            return artistRepository.findByStatus(status, pageable);
        } else {
            return artistRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ArtistWithAlbumCountDTO> finArtistWithAlbumCount(int page, int size, Status status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        return artistRepository.findAllWithAlbumCount(status, pageable);
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<Artist> findById(Long id) {
        return artistRepository.findById(id);
    }

    @Override
    public Artist patchArtist(Long id,
                              ArtistPatchDTO dto,
                              MultipartFile photo) {

        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        if (dto.getName() != null) artist.setName(dto.getName());
        if (dto.getDetails() != null) artist.setDetails(dto.getDetails());
        if (dto.getNationality() != null) artist.setNationality(dto.getNationality());
        if (dto.getStatus() != null) artist.setStatus(dto.getStatus());

        MultipartFile photoFile = dto.getPhoto();
        if (photoFile != null && !photoFile.isEmpty()) {
            String photoUrl = s3Helper.uploadPhoto(photoFile);
            artist.setPhoto(photoUrl);
        }

        userHelper.logActivity("Updated artist: " + artist.getName());
        return artistRepository.save(artist);
    }

    @Override
    public void deleteById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        visitService.deleteByArtistId(id);

        userHelper.logActivity("Delete artist: " + artist.getName());

        artistRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Artist> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist name must not be null or empty");
        }
        return artistRepository.findByName(name.trim());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Artist> search(String term, int page, int size, Status status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        if (term == null || term.trim().isEmpty()) {
            return findAll(page, size, status);
        }

        if (status != null) {
            return artistRepository.searchByTermAndStatus(term.trim(), status, pageable);
        }

        return artistRepository.searchByTerm(term.trim(), pageable);
    }


    @Transactional(readOnly = true)
    @Override
    public Page<Artist> findAllWithAlbums(Pageable pageable) {
        return artistRepository.findAll(pageable);
    }

    @Override
    public long getTotalArtists() {
        return artistRepository.count();
    }

    @Override
    public long getActiveArtist() {
        return artistRepository.countByStatus(Status.ACTIVE);
    }

    @Override
    public long getSuspendedArtists() {
        return artistRepository.countByStatus(Status.SUSPENDED);
    }
}
