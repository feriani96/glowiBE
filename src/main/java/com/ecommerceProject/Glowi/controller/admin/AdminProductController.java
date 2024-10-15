package com.ecommerceProject.Glowi.controller.admin;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.services.admin.adminproduct.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping("products")
    public ResponseEntity<Product> createProduct(
    @RequestParam("name") String name,
    @RequestParam("description") String description,
    @RequestParam("price") float price,
    @RequestParam("quantity") int quantity,
    @RequestParam("colors") List<String> colors,
    @RequestParam("availableSizes") List<String> availableSizes,
    @RequestParam("categoryId") String categoryId,
    @RequestParam(value = "image0", required = false) MultipartFile image0,
    @RequestParam(value = "image1", required = false) MultipartFile image1,
    @RequestParam(value = "image2", required = false) MultipartFile image2,
    @RequestParam(value = "image3", required = false) MultipartFile image3){

        // Créer une liste pour les images
        List<MultipartFile> images = new ArrayList<>();
        if (image0 != null) images.add(image0);
        if (image1 != null) images.add(image1);
        if (image2 != null) images.add(image2);
        if (image3 != null) images.add(image3);

        // Créer un DTO pour le produit
        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setDescription(description);
        productDto.setPrice(price);
        productDto.setQuantity(quantity);
        productDto.setColors(colors);
        productDto.setAvailableSizes(availableSizes);
        productDto.setCategoryId(categoryId);
        productDto.setImages(images); // Passer les images si elles existent

        // Appeler la méthode du service pour créer le produit
        Product createdProduct = adminProductService.createproduct(productDto);

        // Retourner une réponse HTTP avec le produit créé
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
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
