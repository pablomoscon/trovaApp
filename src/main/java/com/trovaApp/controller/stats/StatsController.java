package com.trovaApp.controller.stats;

import com.trovaApp.dto.visit.VisitStatDto;
import com.trovaApp.service.album.AlbumService;
import com.trovaApp.service.artist.ArtistService;
import com.trovaApp.service.user.UserService;
import com.trovaApp.service.visit.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "stats", description = "Operations related to statistics and visit data")
@RestController
@RequestMapping("/stats")
public class StatsController {

    private final UserService userService;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final VisitService visitService;

    @Autowired
    public StatsController(
            UserService userService,
            AlbumService albumService,
            ArtistService artistService,
            VisitService visitService
    ) {
        this.userService = userService;
        this.albumService = albumService;
        this.artistService = artistService;
        this.visitService = visitService;
    }

    @Operation(summary = "Get summary statistics about users, albums, and artists")
    @GetMapping("/summary")
    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("totalUsers", userService.getTotalUsers());
        stats.put("activeUsers", userService.getActiveUsers());
        stats.put("suspendedUsers", userService.getSuspendedUsers());
        stats.put("deletedUsers", userService.getDeletedUsers());

        stats.put("totalAlbums", albumService.getTotalAlbums());
        stats.put("activeAlbums", albumService.getActiveAlbums());
        stats.put("suspendedAlbums", albumService.getSuspendedAlbums());

        stats.put("totalArtists", artistService.getTotalArtists());
        stats.put("activeArtist", artistService.getActiveArtist());
        stats.put("suspendedArtists", artistService.getSuspendedArtists());

        return stats;
    }

    @Operation(summary = "Get list of most visited albums")
    @GetMapping("/most-visited-albums")
    public List<VisitStatDto> getMostVisitedAlbums() {
        return visitService.getMostVisitedAlbums().stream().map(map -> {
            Long albumId = (Long) map.get("albumId");
            int visits = ((Long) map.get("visits")).intValue();
            String title = (String) map.get("title");

            return new VisitStatDto(albumId, null, title, null, visits, null);
        }).toList();
    }

    @Operation(summary = "Get list of most visited artists")
    @GetMapping("/most-visited-artists")
    public List<VisitStatDto> getMostVisitedArtists() {
        return visitService.getMostVisitedArtists().stream().map(map -> {
            Long artistId = (Long) map.get("artistId");
            int visits = ((Long) map.get("visits")).intValue();
            String name = (String) map.get("name");

            return new VisitStatDto(null, artistId, null, name, visits, null);
        }).toList();
    }
}
