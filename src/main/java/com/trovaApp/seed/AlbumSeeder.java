package com.trovaApp.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trovaApp.dto.album.AlbumSeedDTO;
import com.trovaApp.enums.Genre;
import com.trovaApp.model.Album;
import com.trovaApp.model.Artist;
import com.trovaApp.model.Song;
import com.trovaApp.service.album.AlbumService;
import com.trovaApp.service.artist.ArtistService;
import com.trovaApp.service.song.SongService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AlbumSeeder {

    private final ObjectMapper objectMapper;
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final SongService songService;

    public AlbumSeeder(ObjectMapper objectMapper,
                       ArtistService artistService,
                       AlbumService albumService,
                       SongService songService) {
        this.objectMapper = objectMapper;
        this.artistService = artistService;
        this.albumService = albumService;
        this.songService = songService;
    }

    public void seed() throws Exception {
        if (albumService.getTotalAlbums() > 1) return;

        ClassPathResource resource = new ClassPathResource("seed/albums.json");

        try (InputStream is = resource.getInputStream()) {

            List<AlbumSeedDTO> albums = objectMapper.readValue(
                    is, new TypeReference<List<AlbumSeedDTO>>() {});

            for (AlbumSeedDTO dto : albums) {

                Artist artist = artistService.findByName(dto.getArtistName())
                        .orElse(null);
                if (artist == null) {
                    System.out.printf("Artist not found, skipping album: %s%n", dto.getTitle());
                    continue;
                }

                Album album = new Album();
                album.setTitle(dto.getTitle());
                album.setDetails(dto.getDetails());
                album.setCdNumber(dto.getCdNumber());
                album.setPhoto(dto.getPhoto());
                album.setYear(dto.getYear());
                album.setAppleMusicLink(dto.getAppleMusicLink());
                album.setAmazonMusicLink(dto.getAmazonMusicLink());
                album.setSpotifyLink(dto.getSpotifyLink());

                Set<Genre> genreSet = dto.getGenres() != null
                        ? dto.getGenres().stream()
                        .map(String::toUpperCase)
                        .map(Genre::valueOf)
                        .collect(Collectors.toSet())
                        : Collections.emptySet();
                album.setGenres(genreSet);

                album.setArtist(artist);
                album.setDisplayArtistName(dto.getArtistName());

                album.setAppleMusicLink(dto.getAppleMusicLink());
                album.setAmazonMusicLink(dto.getAmazonMusicLink());
                album.setSpotifyLink(dto.getSpotifyLink());

                album = albumService.saveRaw(album);

                if (dto.getListOfSongs() != null && !dto.getListOfSongs().isEmpty()) {
                    List<Song> songs = songService.create(dto.getListOfSongs(), album, artist);
                    album.setListOfSongs(songs);
                    albumService.saveRaw(album);
                }

                System.out.printf("Seeded album: %s%n", album.getTitle());
            }

            System.out.printf("Seeded %d albums%n", albums.size());
        }
    }
}
