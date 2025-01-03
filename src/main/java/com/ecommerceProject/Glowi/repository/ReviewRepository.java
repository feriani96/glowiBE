package com.ecommerceProject.Glowi.repository;


import com.ecommerceProject.Glowi.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findAllByProductId (String productId);
}
