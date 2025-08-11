package com.trovaApp.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class AlbumUtils {

    // Build pageable based on sort order
    public static Pageable buildPageRequest(int page, int size, String sortOrder) {
        if (sortOrder == null || sortOrder.isEmpty()) {
            return PageRequest.of(page, size, Sort.by(Sort.Order.asc("artist.name")));
        }

        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, "year"));
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
    public static <T> List<T> orderByIds(List<Long> ids, List<T> items, java.util.function.Function<T, Long> idExtractor) {
        Map<Long, T> itemMap = items.stream()
                .collect(Collectors.toMap(idExtractor, i -> i));

        return ids.stream()
                .map(itemMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
