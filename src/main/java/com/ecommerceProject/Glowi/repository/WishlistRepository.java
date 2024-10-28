package com.ecommerceProject.Glowi.repository;

import com.ecommerceProject.Glowi.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {

    List<Wishlist> findAllByUserId(String userId);
}
