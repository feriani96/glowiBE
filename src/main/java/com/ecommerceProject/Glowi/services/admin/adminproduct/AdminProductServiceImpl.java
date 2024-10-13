package com.ecommerceProject.Glowi.services.admin.adminproduct;

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

        product.setImgUrls(productDto.getImgUrls());

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
