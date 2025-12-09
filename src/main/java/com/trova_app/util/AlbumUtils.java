package com.trova_app.util;

import com.trova_app.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlbumUtils {

    private static final String ARTIST_NAME = "artist.name";
    private static final String TITLE = "title";
    private static final String YEAR = "year";

    // Build pageable based on sort order
    public static Pageable buildPageRequest(int page, int size, String sortOrder) {
        if (sortOrder == null || sortOrder.isBlank()) {
            return PageRequest.of(page, size,
                    Sort.by(Sort.Order.asc(ARTIST_NAME))
                            .and(Sort.by(Sort.Order.asc(TITLE))));
        }

        switch (sortOrder.trim().toLowerCase()) {
            case "artist":
                return PageRequest.of(page, size,
                        Sort.by(Sort.Order.asc(ARTIST_NAME))
                                .and(Sort.by(Sort.Order.asc(TITLE))));
            case "title":
                return PageRequest.of(page, size, Sort.by(TITLE).ascending());
            case "asc":
                return PageRequest.of(page, size, Sort.by(YEAR).ascending());
            case "desc":
                return PageRequest.of(page, size, Sort.by(YEAR).descending());
            default:
                return PageRequest.of(page, size,
                        Sort.by(Sort.Order.asc(ARTIST_NAME))
                                .and(Sort.by(Sort.Order.asc(TITLE))));
        }
    }

    // Normalize artist names to lowercase
    public static List<String> normalizeArtistNames(List<String> artistNames) {
        if (artistNames == null || artistNames.isEmpty()) {
            return null;
        }

        return artistNames.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    // Order albums based on list of IDs
    public static <T> List<T> orderByIds(
            List<Long> ids,
            List<T> items,
            Function<T, Long> idExtractor
    ) {
        Map<Long, T> itemMap = items.stream()
                .collect(Collectors.toMap(idExtractor, Function.identity()));

        return ids.stream()
                .map(itemMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // Order albums by title
    public static List<Album> orderByTitle(List<Album> items) {
        return items.stream()
                .sorted(Comparator.comparing(Album::getTitle, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public static Map<String, Object> buildPagedResponse(String key, Page<?> page, List<?> content) {
        return Map.of(
                key, content,
                "currentPage", page.getNumber(),
                "totalItems", page.getTotalElements(),
                "totalPages", page.getTotalPages()
        );
    }
}
