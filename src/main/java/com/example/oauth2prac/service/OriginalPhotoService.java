package com.example.oauth2prac.service;

import com.example.oauth2prac.entity.OriginalImage;
import com.example.oauth2prac.entity.User;
import com.example.oauth2prac.repository.OriginalImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OriginalPhotoService {
    private final MinioService minioService;

    public void uploadOriginalPhoto(MultipartFile file, String objectName, User user) throws Exception{
        minioService.uploadFile(objectName,file);

        OriginalImage originalImage = OriginalImage.builder()
                .imageUrl(minioService.getPresignedUrl(objectName))
                .user(user)
                .fileName(objectName)
                .build();

        user.getOriginalImages().add(originalImage);
    }
}
