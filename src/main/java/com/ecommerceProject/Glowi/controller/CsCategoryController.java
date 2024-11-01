package com.ecommerceProject.Glowi.controller;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Category;
import com.ecommerceProject.Glowi.services.customer.category.CustomerCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CsCategoryController {

    private final CustomerCategoryService customerCategoryService;


    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = customerCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable String categoryId ) {
        List<ProductDto> products = customerCategoryService.getProductsByCategory(categoryId );
        return ResponseEntity.ok(products);
    }
}
