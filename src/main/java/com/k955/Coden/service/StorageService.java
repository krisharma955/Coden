package com.k955.Coden.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void upload(String objectKey, MultipartFile file);
    void delete(String objectKey);
}
