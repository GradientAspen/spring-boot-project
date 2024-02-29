package com.project.springbootproject.dto.order;

import com.project.springbootproject.dto.orderitemdto.OrderItemResponseDto;
import com.project.springbootproject.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Order.Status status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private Set<OrderItemResponseDto> orderItems;
}
