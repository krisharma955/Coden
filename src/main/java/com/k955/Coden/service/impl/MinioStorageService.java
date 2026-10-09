package com.k955.Coden.service.impl;

import com.k955.Coden.exception.StorageException;
import com.k955.Coden.service.StorageService;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @PostConstruct
    void ensureBucket() {
        ensureBucketQuietly();
    }

    @Override
    public void upload(String objectKey, MultipartFile file) {
        ensureBucketQuietly();   // bucket may be missing if MinIO was down at startup
        try (InputStream in = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .stream(in, file.getSize(), -1)
                    .contentType(file.getContentType() != null
                            ? file.getContentType() : "application/octet-stream")
                    .build());
        } catch (Exception e) {
            throw new StorageException("Failed to upload file", e);
        }
    }

    @Override
    public InputStream download(String objectKey) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build());
        } catch (Exception e) {
            throw new StorageException("Failed to download file " + objectKey, e);
        }
    }

    private void ensureBucketQuietly() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            // don't prevent app startup when MinIO is unreachable; putObject surfaces the real error
            log.warn("Could not ensure bucket '{}'", bucket, e);
        }
    }

    @Override
    public void delete(String objectKey) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket).object(objectKey).build());
        } catch (Exception e) {
            log.error("Failed to delete object {}", objectKey, e);   // don't throw from cleanup
        }
    }
}
