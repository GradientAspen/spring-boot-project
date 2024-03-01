package com.project.springbootproject.mapper;

import com.project.springbootproject.config.MapperConfig;
import com.project.springbootproject.dto.order.OrderDto;
import com.project.springbootproject.dto.orderitemdto.OrderItemResponseDto;
import com.project.springbootproject.model.Order;
import com.project.springbootproject.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {

    @Mapping(source = "user.id",target = "userId")
    OrderDto toDto(Order order);

    Order toModel(OrderDto orderDto);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "quantity", target = "bookQuantity")
    @Mapping(source = "id", target = "id")
    OrderItemResponseDto orderItemToOrderItemResponseDto(OrderItem orderItem);
}
