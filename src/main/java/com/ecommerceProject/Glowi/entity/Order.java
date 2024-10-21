package com.ecommerceProject.Glowi.entity;

import com.ecommerceProject.Glowi.dto.OrderDto;
import com.ecommerceProject.Glowi.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
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
    private Long amount = 0L;
    private Long totalAmount = 0L;
    private String address;
    private String payment;
    private OrderStatus orderStatus;
    private Long discount;
    private String trackingId;

    private User user;

    private Coupon coupon;

    private List<CartItems> cartItems;

    public OrderDto getOrderDto(){
        OrderDto orderDto = new OrderDto();

        orderDto.setId(id);
        orderDto.setOrderDescription(orderDescription);
        orderDto.setAddress(address);
        orderDto.setTrackingId(trackingId);
        orderDto.setAmount(amount);
        orderDto.setDate(date);
        orderDto.setOrderStatus(orderStatus);
        orderDto.setUserName(user.getName());
        if(coupon != null){
            orderDto.setCouponName(coupon.getName());
        }

        return orderDto;
    }

}
