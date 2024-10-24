package com.ecommerceProject.Glowi.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PlaceOrderDto {

    @Id
    private String userId;
    private String address;
    private String orderDescription;
}
