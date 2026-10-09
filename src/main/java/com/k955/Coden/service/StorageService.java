package com.k955.Coden.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface StorageService {
    void upload(String objectKey, MultipartFile file);
    InputStream download(String objectKey);
    void delete(String objectKey);
}
