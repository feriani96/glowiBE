package com.ecommerceProject.Glowi.services.customer.wishlist;

import com.ecommerceProject.Glowi.dto.WishlistDto;

import java.util.List;

public interface WishlistService {
    WishlistDto addProductToWishlist(WishlistDto wishlistDto);

    List<WishlistDto> getWishlistByUserId(String userId);
}
