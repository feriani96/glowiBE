package com.ecommerceProject.Glowi.entity;

import com.ecommerceProject.Glowi.dto.WishlistDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Wishlist")
@TypeAlias("Wishlist")
@Data
public class Wishlist {

    @Id
    private String id;

    @DBRef
    private Product product;

    @DBRef
    private User user;

    public WishlistDto getWishlistDto(){
        WishlistDto wishlistDto = new WishlistDto();

        wishlistDto.setId(id);
        wishlistDto.setProductId(product.getId());
        wishlistDto.setProductName(product.getName());
        wishlistDto.setProductDescription(product.getDescription());
        wishlistDto.setPrice(product.getPrice());
        wishlistDto.setUserId(user.getId());

        if (product != null && product.getDto() != null) {
            List<String> images = product.getDto().getImageUrls();
            if (images != null && !images.isEmpty()) {
                wishlistDto.setImageUrls(images);
                wishlistDto.setReturnedImg(images.get(0));
            }
        }

        return wishlistDto;


    }

}
