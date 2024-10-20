package com.ecommerceProject.Glowi.controller.customer;

import com.ecommerceProject.Glowi.dto.AddProductInCartDto;
import com.ecommerceProject.Glowi.dto.OrderDto;
import com.ecommerceProject.Glowi.services.customer.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("cart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductInCartDto addProductInCartDto) {
        return cartService.addProductToCart(addProductInCartDto);
    }

    @GetMapping("cart/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable String userId) {
        OrderDto orderDto = cartService.getCartByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }
}
