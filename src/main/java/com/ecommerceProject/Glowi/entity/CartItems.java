package com.ecommerceProject.Glowi.entity;


import com.ecommerceProject.Glowi.dto.CartItemsDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cartItems")
@TypeAlias("CartItems")
@Data
public class CartItems {

    @Id
    private String id;

    private Float price;

    private int quantity;

    private Product product;

    @DBRef
    private User user;

    @DBRef
    private Order order;


    public CartItemsDto getCartDto(){
        CartItemsDto cartItemsDto = new CartItemsDto();
        cartItemsDto.setId(id);
        cartItemsDto.setPrice(price);
        cartItemsDto.setProductId(product.getId());
        cartItemsDto.setQuantity(quantity);
        cartItemsDto.setUserId(user.getId());
        cartItemsDto.setProductName(product.getName());
        cartItemsDto.setImages(product.getDto().getImages());

        return cartItemsDto;
    }


}
