package com.ecommerceProject.Glowi.entity;


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

    private float price;

    private int quantity;

    @DBRef
    private Product product;

    @DBRef
    private User user;

    @DBRef
    private Order order;

}
