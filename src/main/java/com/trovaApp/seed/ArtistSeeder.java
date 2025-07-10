package com.trovaApp.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trovaApp.model.Artist;
import com.trovaApp.repository.ArtistRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class ArtistSeeder {

    private final ArtistRepository artistRepository;
    private final ObjectMapper objectMapper;

    public ArtistSeeder(ArtistRepository artistRepository, ObjectMapper objectMapper) {
        this.artistRepository = artistRepository;
        this.objectMapper = objectMapper;
    }

    public void seed() throws Exception {
        if (artistRepository.count() > 1) return;

        ClassPathResource resource = new ClassPathResource("seed/artists.json");
        try (InputStream is = resource.getInputStream()) {
            List<Artist> artists = objectMapper.readValue(is, new TypeReference<List<Artist>>() {});
            artistRepository.saveAll(artists);
            System.out.printf("Seeded %d artists%n", artists.size());
        }
    }
}
