package com.ecommerceProject.Glowi.services.admin.adminOrder;

import com.ecommerceProject.Glowi.dto.AnalyticsResponse;
import com.ecommerceProject.Glowi.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {

    List<OrderDto> getAllPlacedOrders();

    OrderDto changeOrderStatus(String orderId, String status);

    AnalyticsResponse calculateAnalytics();
}
