package com.ecommerceProject.Glowi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    private String categoryId;
    private String categoryName;

    private List<MultipartFile> images;

    private List<String> imageUrls;
}
