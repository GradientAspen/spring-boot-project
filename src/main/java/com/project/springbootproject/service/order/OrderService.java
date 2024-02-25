package com.project.springbootproject.service.order;

import com.project.springbootproject.dto.order.OrderDto;
import com.project.springbootproject.dto.orderitemdto.OrderItemResponseDto;
import com.project.springbootproject.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderDto updateOrderStatus(Long id, Order.Status status);

    OrderItemResponseDto getAllOrderItems(OrderDto orderDto);

    OrderDto creatOrder(String shoppingAddress, Long userId );

    List<OrderDto> getAllUsersOrders(Long userId);


}
