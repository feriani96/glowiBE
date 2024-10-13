package com.ecommerceProject.Glowi.controller.admin;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.services.admin.adminproduct.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("products")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        return ResponseEntity.ok(adminProductService.getAllProducts());
    }


    @GetMapping("products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id){
        Product product = adminProductService.getProductById(id);
        return ResponseEntity.ok(product);
    }


}
