package com.ecommerceProject.Glowi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Wishlist")
@TypeAlias("Wishlist")
@Data
public class Wishlist {

    @Id
    private String id;

    @DBRef
    private Product product;

    @DBRef
    private User user;

}
