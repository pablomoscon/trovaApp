package com.trovaApp.service.artist;

import com.trovaApp.dto.artist.ArtistCreateDTO;
import com.trovaApp.dto.artist.ArtistPatchDTO;
import com.trovaApp.exception.ArtistNotFoundException;
import com.trovaApp.model.Artist;
import com.trovaApp.repository.ArtistRepository;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public Artist create(ArtistCreateDTO dto) {
        Artist artist = new Artist();
        artist.setName(dto.getName());
        artist.setDetails(dto.getDetails());
        artist.setNationality(dto.getNationality());
        artist.setPhoto(dto.getPhoto());

        return artistRepository.save(artist);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Artist> findById(Long id) {
        return artistRepository.findWithAlbumsById(id);
    }

    @Override
    public Artist patchArtist(Long id, ArtistPatchDTO dto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found"));

        if (dto.getName() != null) artist.setName(dto.getName());
        if (dto.getDetails() != null) artist.setDetails(dto.getDetails());
        if (dto.getNationality() != null) artist.setNationality(dto.getNationality());
        if (dto.getPhoto() != null) artist.setPhoto(dto.getPhoto());

        return artistRepository.save(artist);
    }

    @Override
    public void deleteById(Long id) {
        artistRepository.deleteById(id);
    }

}
