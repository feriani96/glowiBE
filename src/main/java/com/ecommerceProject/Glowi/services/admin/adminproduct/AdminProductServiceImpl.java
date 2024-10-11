package com.ecommerceProject.Glowi.services.admin.adminproduct;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Category;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.repository.CategoryRepository;
import com.ecommerceProject.Glowi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

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
        product.setImgUrls(productDto.getImgUrls()); // Utiliser la liste d'URLs
        product.setCategoryId(categoryId);
        product = productRepository.save(product);
        product.setCategory(category);
        return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
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
