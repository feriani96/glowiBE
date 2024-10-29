package com.ecommerceProject.Glowi.controller.customer;

import com.ecommerceProject.Glowi.dto.AddProductInCartDto;
import com.ecommerceProject.Glowi.dto.OrderDto;
import com.ecommerceProject.Glowi.dto.PlaceOrderDto;
import com.ecommerceProject.Glowi.exceptions.ValidationException;
import com.ecommerceProject.Glowi.services.customer.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("cart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductInCartDto addProductInCartDto) {
        return cartService.addProductToCart(addProductInCartDto);
    }

    @DeleteMapping("cart/{userId}/{productId}")
    public ResponseEntity<?> deleteProductsFromCart(@PathVariable String userId, @PathVariable String productId) {
        cartService.deleteProductsFromCart(userId, productId);
        OrderDto updatedCart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping("cart/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable String userId) {
        try {
            OrderDto orderDto = cartService.getCartByUserId(userId);
            System.out.println("OrderDto: " + orderDto);
            if (orderDto != null && !orderDto.getCartItems().isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(orderDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No items found in cart for this user.");
            }
        } catch (Exception e) {
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

    @PostMapping("/addition")
    public ResponseEntity<OrderDto> increaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.increaseProductQuantity(addProductInCartDto));
    }

    @PostMapping("/deduction")
    public ResponseEntity<OrderDto> decreaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.decreaseProductQuantity(addProductInCartDto));
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderDto placeOrderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.placeOrder(placeOrderDto));
    }

    @GetMapping("/myOrders/{userId}")
    public ResponseEntity<List<OrderDto>> getMyPlacedOrders(@PathVariable String userId){
        return ResponseEntity.ok(cartService.getMyPlacedOrders(userId));
    }

}
