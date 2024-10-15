package com.ecommerceProject.Glowi.services.admin.adminproduct;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Category;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.repository.CategoryRepository;
import com.ecommerceProject.Glowi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private static final String UPLOAD_DIR = "src/main/resources/uploads";


    public Product createproduct(ProductDto productDto) {
        String categoryId = productDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setColors(productDto.getColors());
        product.setAvailableSizes(productDto.getAvailableSizes());

        // Créer le dossier de stockage d'images si nécessaire
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);  // Crée le dossier si non existant
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory for file upload", e);
            }
        }

        // Gérer l'upload des images uniquement si elles sont présentes
        if (productDto.getImages() != null && !productDto.getImages().isEmpty()) {
            List<String> imgUrls = new ArrayList<>();
            for (MultipartFile image : productDto.getImages()) {
                if (!image.isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);  // Résoudre le chemin complet

                    try {
                        // Sauvegarder le fichier sur le serveur
                        Files.write(filePath, image.getBytes());
                        // Ajouter l'URL de l'image à la liste
                        imgUrls.add("/uploads/" + fileName);  // Utiliser un chemin relatif pour l'URL
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Failed to store image", e);
                    }
                }
            }
            // Si des images ont été téléchargées, les ajouter au produit
            if (!imgUrls.isEmpty()) {
                product.setImgUrls(imgUrls);  // Assurez-vous que Product a un champ imgUrls
            }
        }

        product.setCategoryId(categoryId);
        product.setCategoryName(category.getName());
        product = productRepository.save(product);
        product.setCategory(category);
        return product;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            throw new RuntimeException("No products found.");
        }

        return products.stream()
            .map(product -> {
                Category category = categoryRepository.findById(product.getCategoryId())
                    .orElse(null);

                if (category != null) {
                    product.setCategory(category);
                } else {
                    System.out.println("Category not found for product: " + product.getId());
                }
                return product.getDto();
            })
            .collect(Collectors.toList());
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
            .map(product -> {
                Category category = categoryRepository.findById(product.getCategoryId())
                    .orElse(null);
                product.setCategory(category);
                return product;
            })
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }
}
