package com.trovaApp.service.aws;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface S3Service {
    String uploadFileToS3(MultipartFile file);
}
