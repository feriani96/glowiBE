package com.ecommerceProject.Glowi.controller.admin;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.services.admin.adminproduct.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageUploadController {
    private final AdminProductService adminProductService;
    private final String UPLOAD_DIR = System.getProperty("java.io.tmpdir");

    @PostMapping("/upload")
    public ResponseEntity<Product> uploadImageAndProduct(
        @RequestParam(value = "files") MultipartFile[] files, // Accepter plusieurs fichiers
        @RequestParam("name") String productName,
        @RequestParam("description") String description,
        @RequestParam("price") float price,
        @RequestParam("quantity") int quantity,
        @RequestParam("colors") List<String> colors,
        @RequestParam("availableSizes") List<String> availableSizes,
        @RequestParam("categoryId") String categoryId) {

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                try {
                    imageUrls.add(uploadImage(file)); // Ajouter l'URL d'image à la liste
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }
            }
        }

        ProductDto productDto = new ProductDto();
        productDto.setName(productName);
        productDto.setDescription(description);
        productDto.setPrice(price);
        productDto.setQuantity(quantity);
        productDto.setColors(colors);
        productDto.setAvailableSizes(availableSizes);
        productDto.setCategoryId(categoryId);

        Product product = adminProductService.createproduct(productDto);

        return ResponseEntity.ok(product);
    }

    private String uploadImage(MultipartFile file) throws IOException {
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Path path = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
        Files.copy(file.getInputStream(), path);

        return "http://localhost:8080/api/images/" + path.getFileName();
    }
}
