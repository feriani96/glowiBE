package com.ecommerceProject.Glowi.services.customer.cart;

import com.ecommerceProject.Glowi.dto.AddProductInCartDto;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);
}
