package com.ecommerceProject.Glowi.controller.image;

import com.ecommerceProject.Glowi.services.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private static final Logger logger = Logger.getLogger(ImageController.class.getName());

    private final ImageService imageService;

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // Récupération du chemin de l'image
            Path filePath = imageService.getImage(filename);

            // Utilisation de UrlResource pour charger le fichier
            Resource resource = new UrlResource(filePath.toUri());

            // Vérification de la validité de la ressource
            if (resource.exists() && resource.isReadable()) {
                // Détecte le type MIME de l'image
                String contentType = Files.probeContentType(filePath);

                if (contentType == null) {
                    contentType = "application/octet-stream";  // Default content type
                }

                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
            } else {
                throw new RuntimeException("Impossible de lire le fichier !");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de l'image : " + e.getMessage());
        }
    }
}
