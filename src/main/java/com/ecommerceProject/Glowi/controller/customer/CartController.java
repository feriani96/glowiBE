package com.ecommerceProject.Glowi.controller.customer;

import com.ecommerceProject.Glowi.dto.AddProductInCartDto;
import com.ecommerceProject.Glowi.dto.OrderDto;
import com.ecommerceProject.Glowi.exceptions.ValidationException;
import com.ecommerceProject.Glowi.services.customer.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
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
        try {
            OrderDto orderDto = cartService.getCartByUserId(userId);

            if (orderDto != null && !orderDto.getCartItems().isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(orderDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No items found in cart for this user.");
            }
        } catch (Exception e) {
            // Log the exception details (optional)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the cart.");
        }
    }


    @GetMapping("/coupon/{userId}/{code}")
    public ResponseEntity<?> applyCoupon(@PathVariable String userId, @PathVariable String code){
        try {
            OrderDto orderDto = cartService.applyCoupon(userId, code);
            return ResponseEntity.ok(orderDto);
        } catch (ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }




}
