package com.trova_app.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedRunner implements CommandLineRunner {

    private final ArtistSeeder artistSeeder;
    private final AlbumSeeder albumSeeder;
    private final UserSeeder userSeeder;

    public SeedRunner(ArtistSeeder artistSeeder, AlbumSeeder albumSeeder, UserSeeder userSeeder) {
        this.artistSeeder = artistSeeder;
        this.albumSeeder = albumSeeder;
        this.userSeeder = userSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        artistSeeder.seed();
        albumSeeder.seed();
        userSeeder.seed();

    }
}
