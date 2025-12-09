package com.trova_app.util;

import com.trova_app.exception.InvalidImageException;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    public static void validateImageFile(@org.jetbrains.annotations.NotNull MultipartFile file) {

        if (file.isEmpty()) {
            throw new InvalidImageException("No file provided");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidImageException("File is not an image");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new InvalidImageException("File size exceeds 5MB");
        }
    }
}
