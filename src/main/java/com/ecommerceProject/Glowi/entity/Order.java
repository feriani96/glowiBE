package com.ecommerceProject.Glowi.entity;

import com.ecommerceProject.Glowi.enums.OrderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "orders")
@TypeAlias("Orders")
@Data
public class Order {

    @Id
    private String id;

    private String orderDescription;

    private Date date;

    private Long amount;

    private String address;

    private String payment;

    private OrderStatus orderStatus;

    private Long totalAmount;

    private Long discount;

    private String trackingId;

    @DBRef
    private User user;

    @DBRef
    private List<CartItems> cartItems;
}
