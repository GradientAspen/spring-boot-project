package com.project.springbootproject.service.order;

import com.project.springbootproject.dto.order.OrderDto;
import com.project.springbootproject.dto.orderitemdto.OrderItemResponseDto;
import com.project.springbootproject.model.Order;
import java.util.List;
import java.util.Set;

public interface OrderService {
    OrderDto updateOrderStatus(Long id, Order.Status status);

    Set<OrderItemResponseDto> getAllOrderItemsForOrder(Long orderId);

    OrderDto createOrder(String shoppingAddress, Long userId);

    List<OrderDto> getAllUserOrders(Long userId);

    List<OrderItemResponseDto> getAllCartItemsForOrder(Long orderId);

    OrderItemResponseDto getOrderItemForSpecificOrder(Long orderId, Long orderItemId);
}
