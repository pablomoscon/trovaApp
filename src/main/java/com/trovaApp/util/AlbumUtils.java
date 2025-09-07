package com.trovaApp.util;

import com.trovaApp.model.Album;
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

    // Build pageable based on sort order
    public static Pageable buildPageRequest(int page, int size, String sortOrder) {
        if (sortOrder == null || sortOrder.isBlank()) {
            return PageRequest.of(page, size,
                    Sort.by(Sort.Order.asc("artist.name"))
                            .and(Sort.by(Sort.Order.asc("title"))));
        }

        switch (sortOrder.trim().toLowerCase()) {
            case "artist":
                return PageRequest.of(page, size,
                        Sort.by(Sort.Order.asc("artist.name"))
                                .and(Sort.by(Sort.Order.asc("title"))));
            case "title":
                return PageRequest.of(page, size, Sort.by("title").ascending());
            case "asc":
                return PageRequest.of(page, size, Sort.by("year").ascending());
            case "desc":
                return PageRequest.of(page, size, Sort.by("year").descending());
            default:
                return PageRequest.of(page, size,
                        Sort.by(Sort.Order.asc("artist.name"))
                                .and(Sort.by(Sort.Order.asc("title"))));
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
