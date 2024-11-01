package com.ecommerceProject.Glowi.services.customer.category;

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
public class CustomerCategoryServiceImpl implements CustomerCategoryService{

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


    public List<ProductDto> getProductsByCategory(String categoryId ) {
        List<Product> products = productRepository.findAllByCategoryId(categoryId );
        return products.stream()
            .map(Product::getDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<Category> getFilledCategories() {
        return categoryRepository.findAll().stream()
            .filter(category -> !productRepository.findAllByCategoryId(category.getId()).isEmpty())
            .toList();
    }



}
