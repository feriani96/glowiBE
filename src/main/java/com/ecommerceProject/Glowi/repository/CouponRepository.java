package com.ecommerceProject.Glowi.repository;


import com.ecommerceProject.Glowi.entity.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {

    boolean existsByCode(String code);
}
