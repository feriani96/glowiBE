package com.ecommerceProject.Glowi.repository;

import com.ecommerceProject.Glowi.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

}
