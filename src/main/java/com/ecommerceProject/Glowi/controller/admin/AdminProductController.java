package com.ecommerceProject.Glowi.controller.admin;

import com.ecommerceProject.Glowi.dto.FAQDto;
import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.services.admin.adminproduct.AdminProductService;
import com.ecommerceProject.Glowi.services.admin.faq.FAQService;
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

    private final FAQService faqService;

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
        productDto.setImages(images);

        Product createdProduct = adminProductService.createproduct(productDto);

        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("products")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        return ResponseEntity.ok(adminProductService.getAllProducts());
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductByName(@PathVariable String name){
        return ResponseEntity.ok(adminProductService.getAllProductByName(name));
    }

    @GetMapping("products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id){
        Product product = adminProductService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/product/{productId}")
    public  ResponseEntity<Void> deleteProduct(@PathVariable String productId){
        boolean deleted = adminProductService.deleteProduct(productId);
        if (deleted){
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.notFound().build();
    }

    @PostMapping("/faq/{productId}")
    public ResponseEntity<FAQDto> postFAQ(@PathVariable String productId, @RequestBody FAQDto faqDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.postFAQ(productId, faqDto));
    }



}
