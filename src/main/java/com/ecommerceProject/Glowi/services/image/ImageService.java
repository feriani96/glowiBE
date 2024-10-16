package com.ecommerceProject.Glowi.services.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private static final String UPLOAD_DIR = "uploads";  // Dossier local où stocker les images

    // Injecter l'URL de base depuis application.properties
    @Value("${app.upload.base-url}")
    private String baseUrl;

    // Méthode pour enregistrer plusieurs images
    public List<String> saveImages(List<MultipartFile> images) {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory for file upload", e);
            }
        }

        List<String> imgUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);

                    try {
                        Files.write(filePath, image.getBytes());
                        // Utilisez une URL relative pour l'image
                        String imageUrl = "/uploads/" + fileName;  // Ne pas inclure 'http://localhost:8080'

                        imgUrls.add(imageUrl);

                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Failed to store image", e);
                    }
                }
            }
        }
        return imgUrls;
    }


    // Méthode pour récupérer une image (renvoie un Path vers l'image)
    public Path getImage(String imageName) {
        return Paths.get(UPLOAD_DIR).resolve(imageName);
    }
}
