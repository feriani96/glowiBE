package com.ecommerceProject.Glowi.dto;


import com.ecommerceProject.Glowi.entity.CartItems;
import com.ecommerceProject.Glowi.entity.User;
import com.ecommerceProject.Glowi.enums.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDto {

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

    private String userName;

    private List<CartItemsDto> cartItems;

}
