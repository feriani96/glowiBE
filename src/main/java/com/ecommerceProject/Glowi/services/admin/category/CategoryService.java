package com.ecommerceProject.Glowi.services.admin.category;

import com.ecommerceProject.Glowi.dto.CategoryDto;
import com.ecommerceProject.Glowi.entity.Category;

public interface CategoryService {
    Category createcategory(CategoryDto categoryDto);
}
