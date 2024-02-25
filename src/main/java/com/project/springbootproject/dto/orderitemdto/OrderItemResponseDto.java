package com.project.springbootproject.dto.orderitemdto;

import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Long bookId;
    private int bookQuantity;
}
