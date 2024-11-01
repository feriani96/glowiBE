package com.ecommerceProject.Glowi.services.customer.category;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Category;

import java.util.List;

public interface CustomerCategoryService {
    List<ProductDto> getProductsByCategory(String categoryId );

    List<Category> getAllCategories();

    List<Category> getFilledCategories();
}
