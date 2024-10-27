package com.ecommerceProject.Glowi.entity;

import com.ecommerceProject.Glowi.dto.ReviewDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@TypeAlias("Reviews")
@Data
public class Review {

    @Id
    private String id;

    private Long rating;

    private String description;

    private byte[] img;

    @DBRef
    private User user;

    @DBRef
    private Product product;

    public ReviewDto getDto(){

        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId(id);
        reviewDto.setRating(rating);
        reviewDto.setDescription(description);
        reviewDto.setReturnedImg(img);

        reviewDto.setProductId(product.getId());
        reviewDto.setUserId(user.getId());
        reviewDto.setUserName(user.getName());

        return reviewDto;

    }

}
