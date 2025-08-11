package com.trovaApp.helper;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public class PaginationHelper {

    public static Map<String, Object> buildPagedResponse(String key, Page<?> page, List<?> content) {
        return Map.of(
                key, content,
                "currentPage", page.getNumber(),
                "totalItems", page.getTotalElements(),
                "totalPages", page.getTotalPages()
        );
    }
}
