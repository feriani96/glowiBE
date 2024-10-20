package com.ecommerceProject.Glowi.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CartItemsDto {

    @Id
    private String id;

    private Float price;

    private int quantity;

    private String productId;

    private String orderId;

    private String productName;

    private List<MultipartFile> images;

    private List<String> imageUrls;

    private String userId;
}
