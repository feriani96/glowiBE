package com.ecommerceProject.Glowi.services.customer;

import com.ecommerceProject.Glowi.dto.ProductDto;

import java.util.List;

public interface CustomerProductService {

    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductByTitle(String name);
}
