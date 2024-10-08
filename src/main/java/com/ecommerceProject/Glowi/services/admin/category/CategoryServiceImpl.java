package com.ecommerceProject.Glowi.services.admin.category;

import com.ecommerceProject.Glowi.dto.CategoryDto;
import com.ecommerceProject.Glowi.entity.Category;
import com.ecommerceProject.Glowi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public Category createcategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();

    }
}
