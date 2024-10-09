package com.ecommerceProject.Glowi.services.admin.adminproduct;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Product;

import java.util.List;

public interface AdminProductService {

    Product createproduct(ProductDto productDto);

    List<Product> getAllProducts();

    Product getProductById(String id);
}
