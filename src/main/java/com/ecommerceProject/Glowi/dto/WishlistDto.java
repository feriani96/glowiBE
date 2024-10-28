package com.ecommerceProject.Glowi.dto;

import lombok.Data;

import java.util.List;

@Data
public class WishlistDto {

    private String userId;

    private String productId;

    private String id;

    private String productName;

    private String productDescription;

    private List<String> imageUrls;


    private String returnedImg;

    private Float price;
}
