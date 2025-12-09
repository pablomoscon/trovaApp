package com.trova_app.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;
    private final String BUCKET_NAME = "trova-app"; // Make sure this bucket exists

    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String uploadFileToS3(MultipartFile file) {
        final String fileUrl;

        try {
            // Generate a unique key for the file to avoid collisions
            final String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
            final String key = "albums/" + UUID.randomUUID() + "_" + originalFilename;

            // Set metadata
            final ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // Upload file
            amazonS3.putObject(new PutObjectRequest(BUCKET_NAME, key, file.getInputStream(), metadata));

            // Get direct URL of uploaded file
            fileUrl = amazonS3.getUrl(BUCKET_NAME, key).toString();

            // Optional: use pre-signed URL instead of direct URL
            // URL url = generatePresignedUrl(key);
            // fileUrl = url.toString();

        } catch (IOException e) {
            throw new RuntimeException("Error reading file input stream: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to S3: " + e.getMessage(), e);
        }

        return fileUrl;
    }

    // Generate pre-signed URL for temporary access (optional)
    private URL generatePresignedUrl(String key) {
        final Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // 1 hour

        final GeneratePresignedUrlRequest presignedRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(presignedRequest);
    }
}
