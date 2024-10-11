package com.ecommerceProject.Glowi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private float price;
    private int quantity;
    private List<String> colors;
    private List<String> availableSizes;
    private List<String> imgUrls; // Pour plusieurs URLs
    private String categoryId;
}
