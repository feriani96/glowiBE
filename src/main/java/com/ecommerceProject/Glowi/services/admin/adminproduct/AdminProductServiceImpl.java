package com.ecommerceProject.Glowi.services.admin.adminproduct;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Category;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.repository.CategoryRepository;
import com.ecommerceProject.Glowi.repository.ProductRepository;
import com.ecommerceProject.Glowi.services.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private static final Logger logger = Logger.getLogger(AdminProductServiceImpl.class.getName());

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

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

        if (productDto.getImages() != null && !productDto.getImages().isEmpty()) {
            List<String> imgUrls = imageService.saveImageUrls(productDto.getImages());
            if (!imgUrls.isEmpty()) {
                product.setImgUrls(imgUrls);
            }
        }

        product.setCategoryId(categoryId);
        product.setCategoryName(category.getName());
        product = productRepository.save(product);
        product.setCategory(category);

        logger.info("Produit créé avec ID : " + product.getId());
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
                }

                // Créer le ProductDto
                ProductDto productDto = product.getDto();

                // Ajoute les URLs d'images
                List<String> imgUrls = product.getImgUrls();
                if (imgUrls != null && !imgUrls.isEmpty()) {
                    List<String> fullImageUrls = imgUrls.stream()
                        .map(imgUrl -> imgUrl)
                        .collect(Collectors.toList());
                    productDto.setImageUrls(fullImageUrls);
                }

                return productDto;
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

    public List<ProductDto> getAllProductByName(String name) {
        List<Product> products = productRepository.findAllByNameContaining(name);

        if (products.isEmpty()) {
            throw new RuntimeException("No products found.");
        }

        return products.stream()
            .map(product -> {
                Category category = categoryRepository.findById(product.getCategoryId())
                    .orElse(null);

                if (category != null) {
                    product.setCategory(category);
                }

                // Créer le ProductDto
                ProductDto productDto = product.getDto();

                // Ajoute les URLs d'images
                List<String> imgUrls = product.getImgUrls();
                if (imgUrls != null && !imgUrls.isEmpty()) {
                    List<String> fullImageUrls = imgUrls.stream()
                        .map(imgUrl -> imgUrl)
                        .collect(Collectors.toList());
                    productDto.setImageUrls(fullImageUrls);
                }

                return productDto;
            })
            .collect(Collectors.toList());
    }
}
