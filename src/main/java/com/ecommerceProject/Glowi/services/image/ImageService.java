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

    // Constructor injecting the path from application.properties
    public ImageService(@Value("${images.directory}") String imageDirectory) {
        // Resolve the absolute path based on the current working directory
        this.imageDirectory = Paths.get(imageDirectory).toAbsolutePath().normalize();
        logger.info("Image directory: "  + this.imageDirectory.toString());

        // Check if the directory exists; if not, create it
        try {
            if (!Files.exists(this.imageDirectory)) {
                Files.createDirectories(this.imageDirectory);
                logger.info("Directory created: "+ this.imageDirectory.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while creating image directory", e);
        }
    }

    // Method to process and save image URLs
    public List<String> saveImageUrls(List<MultipartFile> images) {
        List<String> imgUrls = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    // Generate a unique name for the image
                    String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                    logger.info("Saving image: " + fileName);

                    // Construct the URL to access the image
                    String imageUrl = "http://localhost:8080/api/images/" + fileName;
                    imgUrls.add(imageUrl);

                    // Physically save the image to the specified location
                    Path targetLocation = this.imageDirectory.resolve(fileName);
                    try {
                        // Save the image to disk
                        image.transferTo(targetLocation.toFile());
                        logger.info("Image saved at: " + targetLocation.toString());
                    } catch (IOException e) {
                        logger.severe("Error while saving the image: " + e.getMessage());
                        throw new RuntimeException("Unable to save image", e);
                    }
                }
            }
        } else {
            logger.warning("No images to save");
        }
        return imgUrls;
    }

    public Path getImage(String filename) {
        Path filePath = imageDirectory.resolve(filename).normalize();
        System.out.println("Path to image: " + filePath.toString());
        return filePath;
    }

    // Method to delete an image from the directory
    public boolean deleteImage(String fileName) {
        Path targetLocation = this.imageDirectory.resolve(fileName);

        try {
            // Check if the file exists and delete it
            if (Files.exists(targetLocation)) {
                Files.delete(targetLocation);
                logger.info("Image deleted successfully: "  + targetLocation.toString());
                return true;
            } else {
                logger.warning("Image not found for deletion: " + targetLocation.toString());
                return false;
            }
        } catch (IOException e) {
            logger.severe("Error while deleting the image: " + e.getMessage());
            throw new RuntimeException("Error deleting image", e);
        }
    }

    // Method to update an existing image
    public String updateImage(String existingFileName, MultipartFile newImage) {
        // Path of the existing image
        Path existingImagePath = this.imageDirectory.resolve(existingFileName).normalize();

        // Check if the existing image is present
        if (Files.exists(existingImagePath)) {
            try {
                // Delete the existing image
                Files.delete(existingImagePath);
                logger.info("Existing image deleted: " + existingImagePath.toString());

                // Save the new image at the same location
                newImage.transferTo(existingImagePath.toFile()); // Use the same file name
                logger.info("New image saved at: "+ existingImagePath.toString());

                // Generate the URL for the new image
                String newImageUrl = "http://localhost:8080/api/images/" + existingFileName; // Garder le même nom
                logger.info("Updated image URL: " + newImageUrl);

                // Return the updated image URL to be saved in the database
                return newImageUrl;

            } catch (IOException e) {
                logger.severe("Error while updating the image: " + e.getMessage());
                throw new RuntimeException("Unable to update image", e);
            }
        } else {
            logger.warning("Image not found for update: "  + existingFileName);
            throw new RuntimeException("Image not found for update");
        }
    }


}
