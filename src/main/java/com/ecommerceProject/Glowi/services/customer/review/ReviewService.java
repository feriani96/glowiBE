package com.ecommerceProject.Glowi.services.customer.review;

import com.ecommerceProject.Glowi.dto.OrderedProductsResponseDto;
import com.ecommerceProject.Glowi.dto.ReviewDto;

import java.io.IOException;

public interface ReviewService {

    OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(String orderId);

    ReviewDto giveReview(ReviewDto reviewDto) throws IOException;
}
