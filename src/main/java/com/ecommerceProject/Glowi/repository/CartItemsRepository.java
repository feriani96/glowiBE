package com.ecommerceProject.Glowi.repository;

import com.ecommerceProject.Glowi.entity.CartItems;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends MongoRepository<CartItems, String> {
    Optional<CartItems> findByProductIdAndOrderIdAndUserId(String productId, String orderId, String userId);
    List<CartItems> findByOrderId(String orderId);

}
