package com.ecommerceProject.Glowi.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartItemsDto {
    private String id;
    private Float price;
    private int quantity;
    private String productId;
    private String orderId;
    private String productName;
    private List<String> imageUrls;
    private String mainImageUrl;
    private String userId;
}
