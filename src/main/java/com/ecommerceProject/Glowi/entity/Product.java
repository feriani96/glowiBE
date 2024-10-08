package com.ecommerceProject.Glowi.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "products")
@TypeAlias("Product")
@Data
public class Product {
    @Id
    private String id;

    private String name;

    private String description;

    private float price;

    private int quantity;

    private List<String> colors;

    private List<String> availableSizes;

    //private List<String> reviews;


    //private byte[] img;

    //@JsonIgnore
    //private Category category;
}
