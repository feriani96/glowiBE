package com.ecommerceProject.Glowi.repository;

import com.ecommerceProject.Glowi.entity.FAQ;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends MongoRepository<FAQ, String> {

    List<FAQ> findAllByProductId (String productId);
}
