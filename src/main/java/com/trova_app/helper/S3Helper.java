package com.trova_app.helper;

import com.trova_app.service.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3Helper {

    private final S3Service s3Service;

    @Autowired
    public S3Helper(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String uploadPhoto(MultipartFile photo) {
        try {
            return s3Service.uploadFileToS3(photo);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading photo to S3", e);
        }
    }
}
