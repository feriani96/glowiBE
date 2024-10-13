package com.ecommerceProject.Glowi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private List<String> imgUrls;
    private String categoryId;
    private String categoryName;
}
