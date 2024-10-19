package com.ecommerceProject.Glowi.services.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

@Service
public class ImageService {

    private static final Logger logger = Logger.getLogger(ImageService.class.getName());

    private final Path imageDirectory;

    // Injection du chemin depuis application.properties
    public ImageService(@Value("${images.directory}") String imageDirectory) {
        // Résolution du chemin absolu en fonction du répertoire de travail actuel
        this.imageDirectory = Paths.get(imageDirectory).toAbsolutePath().normalize();
        logger.info("Répertoire des images : " + this.imageDirectory.toString());

        // Vérifier si le répertoire existe, sinon le créer
        try {
            if (!Files.exists(this.imageDirectory)) {
                Files.createDirectories(this.imageDirectory);
                logger.info("Répertoire créé : " + this.imageDirectory.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la création du répertoire d'images", e);
        }
    }

    // Méthode pour traiter et sauvegarder les URLs des images
    public List<String> saveImageUrls(List<MultipartFile> images) {
        List<String> imgUrls = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    // Génération d'un nom unique pour l'image
                    String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                    logger.info("Enregistrement de l'image : " + fileName);

                    // Construit l'URL pour accéder à l'image
                    String imageUrl = "http://localhost:8080/api/images/" + fileName;
                    imgUrls.add(imageUrl);

                    // Sauvegarde physique de l'image à l'emplacement spécifié
                    Path targetLocation = this.imageDirectory.resolve(fileName);
                    try {
                        // Sauvegarder l'image sur le disque
                        image.transferTo(targetLocation.toFile());
                        logger.info("Image sauvegardée à : " + targetLocation.toString());
                    } catch (IOException e) {
                        logger.severe("Erreur lors de la sauvegarde de l'image : " + e.getMessage());
                        throw new RuntimeException("Impossible de sauvegarder l'image", e);
                    }
                }
            }
        } else {
            logger.warning("Aucune image à enregistrer");
        }
        return imgUrls;
    }

    public Path getImage(String filename) {
        Path filePath = imageDirectory.resolve(filename).normalize();
        System.out.println("Path to image: " + filePath.toString());  // Debugging line
        return filePath;
    }

    // Méthode pour supprimer une image du répertoire
    public boolean deleteImage(String fileName) {
        Path filePath = imageDirectory.resolve(fileName).normalize();
        try {
            // Vérifie si le fichier existe
            if (Files.exists(filePath)) {
                // Supprimer l'image
                Files.delete(filePath);
                logger.info("Image supprimée : " + filePath.toString());
                return true;
            } else {
                logger.warning("L'image n'existe pas : " + filePath.toString());
            }
        } catch (IOException e) {
            logger.severe("Erreur lors de la suppression de l'image : " + e.getMessage());
        }
        return false;
    }
}
