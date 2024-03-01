package com.project.springbootproject.dto.order;

import com.project.springbootproject.model.Order;
import lombok.Data;

@Data
public class OrderDtoStatus {
    private Order.Status status;
}
