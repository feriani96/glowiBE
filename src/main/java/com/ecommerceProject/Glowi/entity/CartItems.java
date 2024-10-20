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

    private Float price;

    private int quantity;

    private Product product;

    @DBRef
    private User user;

    @DBRef
    private Order order;



}
