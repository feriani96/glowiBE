package com.ecommerceProject.Glowi.controller.admin;

import com.ecommerceProject.Glowi.dto.CategoryDto;
import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Category;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.services.admin.adminproduct.AdminProductService;
import com.ecommerceProject.Glowi.services.admin.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping("products")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
        Product product = adminProductService.createproduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(adminProductService.getAllProducts());

    }
}
