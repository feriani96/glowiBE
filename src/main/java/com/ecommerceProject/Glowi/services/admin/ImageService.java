package com.ecommerceProject.Glowi.services.admin;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final String UPLOAD_DIR = System.getProperty("java.io.tmpdir");  // Chemin temporaire

    public String uploadImage(MultipartFile file) throws IOException {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();  // Crée le répertoire si nécessaire
        }

        // Enregistre l'image dans le dossier temporaire
        Path path = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
        Files.copy(file.getInputStream(), path);

        // Retourne l'URL de l'image
        return "http://localhost:8080/api/images/" + path.getFileName();
    }
}
