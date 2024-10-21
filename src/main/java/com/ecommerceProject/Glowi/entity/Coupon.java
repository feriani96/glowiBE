package com.ecommerceProject.Glowi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.util.Date;

@Document(collection = "coupons")
@TypeAlias("Coupon")
@Data
public class Coupon {

    @Id
    private String id;

    private String name;

    private String code;

    private Long discount;

    private Date expirationDate;


}
