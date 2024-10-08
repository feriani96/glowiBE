package com.ecommerceProject.Glowi.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "products")
@TypeAlias("Product")
@Data
public class Product {
    @Id
    private String id;

    private String name;

    @Field("price")
    private float price;

    @Field("quantity")
    private int quantity;

    private String description;

    private List<String> availableSizes;

    private List<String> reviews;

    private List<String> colors;

    private byte[] img;

    @JsonIgnore
    private Category category;
}
