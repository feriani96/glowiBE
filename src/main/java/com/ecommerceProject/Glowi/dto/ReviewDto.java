package com.ecommerceProject.Glowi.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewDto {
    @Id
    private String id;

    private Long rating;

    private String description;

    private MultipartFile img;
    private byte[] returnedImg;


    private String userId;
    private String productId;

    private String userName;
}
