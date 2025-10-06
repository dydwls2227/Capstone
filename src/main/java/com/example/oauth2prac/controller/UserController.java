package com.example.oauth2prac.controller;

import com.example.oauth2prac.entity.User;
import com.example.oauth2prac.repository.OriginalImageRepository;
import com.example.oauth2prac.repository.UserRepository;
import com.example.oauth2prac.service.MinioService;
import com.example.oauth2prac.service.OriginalPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final OriginalPhotoService originalPhotoService;
    private final MinioService minioService;

    @PostMapping("/image")
    public void uploadImage(@RequestParam("file") MultipartFile file,@PathVariable Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow();

        String objectName = file.getOriginalFilename();

        originalPhotoService.uploadOriginalPhoto(file,objectName,user);

    }
}
