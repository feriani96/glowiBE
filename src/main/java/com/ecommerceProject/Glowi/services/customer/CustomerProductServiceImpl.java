package com.ecommerceProject.Glowi.services.customer;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Category;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.repository.CategoryRepository;
import com.ecommerceProject.Glowi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


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
                ProductDto productDto = product.getDto();
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

    public List<ProductDto> getAllProductByTitle(String name) {
        List<Product> products = productRepository.findAllByNameContainingIgnoreCase(name);

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

                // Create the ProductDto
                ProductDto productDto = product.getDto();

                // Add image URLs
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
