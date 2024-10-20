package com.ecommerceProject.Glowi.entity;

import com.ecommerceProject.Glowi.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "orders")
@TypeAlias("Orders")
@Data
public class Order {

    @Id
    private String id;

    private String orderDescription;
    private Date date;
    private Long amount = 0L;
    private Long totalAmount = 0L;
    private String address;
    private String payment;
    private OrderStatus orderStatus;
    private Long discount;
    private String trackingId;

    private User user;


}
