package com.trovaApp.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.net.URL;
import java.util.Date;

@Service
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;
    private final String BUCKET_NAME = "trova-app"; // Bucket name, make sure it's set correctly

    // Constructor injects AmazonS3 client, so it's available for use in this class
    public S3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String uploadFileToS3(MultipartFile file) {
        String fileUrl = null;

        try {
            // Generate a unique key for the file to avoid collisions
            String key = "albums/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Set the file's metadata, like content type and size
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // Upload the file to the specified bucket with the generated key
            amazonS3.putObject(
                    new PutObjectRequest(BUCKET_NAME, key, file.getInputStream(), metadata)
            );

            // Get the URL of the uploaded file (direct access link)
            fileUrl = amazonS3.getUrl(BUCKET_NAME, key).toString();

            // For a temporary, expiring URL, uncomment the following lines:
            // URL url = generatePresignedUrl(key);
            // fileUrl = url.toString();

        } catch (IOException e) {
            // Log and throw an error if there's an issue reading the file stream
            throw new RuntimeException("Error reading file input stream: " + e.getMessage(), e);
        } catch (Exception e) {
            // Log and throw a general error if anything else fails during the upload
            throw new RuntimeException("Error uploading file to S3: " + e.getMessage(), e);
        }

        return fileUrl;
    }

    // Optional method to generate a pre-signed URL for temporary access to the file
    private URL generatePresignedUrl(String key) {

        // Set the expiration time (1 hour in this case)
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;  // 1 hour expiration time
        expiration.setTime(expTimeMillis);

        // Create a pre-signed URL request with GET method and expiration
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        // Generate and return the pre-signed URL
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }
}
