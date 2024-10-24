package com.ecommerceProject.Glowi.repository;


import com.ecommerceProject.Glowi.entity.Order;
import com.ecommerceProject.Glowi.enums.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    Order findByUserIdAndOrderStatus(String userId, OrderStatus orderStatus);

    List<Order> findAllByOrderStatusIn(List<OrderStatus> orderStatusList);

    List<Order> findByUserIdAndOrderStatusIn(String userId, List<OrderStatus> orderStatus);

}
