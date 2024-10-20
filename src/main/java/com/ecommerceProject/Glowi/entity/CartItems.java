package com.ecommerceProject.Glowi.entity;


import com.ecommerceProject.Glowi.dto.CartItemsDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "cartItems")
@TypeAlias("CartItems")
@Data
public class CartItems {

    @Id
    private String id;

    private Float price;

    private int quantity;

    @DBRef
    private Product product;

    @DBRef
    private User user;

    @DBRef
    private Order order;

    public CartItemsDto getCartDto() {
        CartItemsDto cartItemsDto = new CartItemsDto();
        cartItemsDto.setId(id);
        cartItemsDto.setPrice(price);
        cartItemsDto.setProductId(product != null ? product.getId() : null);
        cartItemsDto.setQuantity(quantity);
        cartItemsDto.setUserId(user != null ? user.getId() : null);
        cartItemsDto.setProductName(product != null ? product.getName() : "Unknown");

        if (product != null && product.getDto() != null) {
            List<String> images = product.getDto().getImageUrls();
            if (images != null && !images.isEmpty()) {
                cartItemsDto.setImageUrls(images);
                cartItemsDto.setMainImageUrl(images.get(0));
            }
        }
        return cartItemsDto;
    }




}
