package com.ecommerceProject.Glowi.services.customer.cart;

import com.ecommerceProject.Glowi.dto.AddProductInCartDto;
import com.ecommerceProject.Glowi.dto.OrderDto;
import org.springframework.http.ResponseEntity;

public interface CartService {

    ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);

    OrderDto getCartByUserId(String userId);

    OrderDto applyCoupon(String userId, String code);

    OrderDto IncreaseProductQuantity(AddProductInCartDto addProductInCartDto);


}
