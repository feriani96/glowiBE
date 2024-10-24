package com.ecommerceProject.Glowi.repository;

import com.ecommerceProject.Glowi.entity.FAQ;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQRepository extends MongoRepository<FAQ, String> {
}
