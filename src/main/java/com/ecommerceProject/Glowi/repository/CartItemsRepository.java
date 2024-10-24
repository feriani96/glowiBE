package com.ecommerceProject.Glowi.repository;

import com.ecommerceProject.Glowi.entity.CartItems;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsRepository extends MongoRepository<CartItems, String> {
    CartItems findByProductIdAndOrderIdAndUserId(String productId, String orderId, String userId);
    List<CartItems> findByOrderId(String orderId);

}
