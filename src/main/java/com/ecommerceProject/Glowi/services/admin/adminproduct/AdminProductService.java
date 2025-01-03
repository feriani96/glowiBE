package com.ecommerceProject.Glowi.services.admin.adminproduct;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminProductService {

    Product createproduct(ProductDto productDto);

    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductByName(String name);

    boolean deleteProduct(String id);

    // Product getProductById(String id);

    ProductDto getProductById(String productId);

    ProductDto updateProduct(String productId, ProductDto productDto, List<MultipartFile> newImages);
}
