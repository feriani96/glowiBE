package com.ecommerceProject.Glowi.services.admin.category;

import com.ecommerceProject.Glowi.dto.CategoryDto;
import com.ecommerceProject.Glowi.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createcategory(CategoryDto categoryDto);

    List<Category> getAllCategories();
}
