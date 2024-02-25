package com.project.springbootproject.dto.order;

import com.project.springbootproject.model.Order;
import com.project.springbootproject.model.OrderItem;
import com.project.springbootproject.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDto {
    private Long id;
    private User user;
    private Order.Status status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private Set<OrderItem>orderItems;

}
