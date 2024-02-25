package com.project.springbootproject.mapper;

import com.project.springbootproject.config.MapperConfig;
import com.project.springbootproject.dto.order.OrderDto;
import com.project.springbootproject.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderDto toDto(Order order);

    Order toModel(OrderDto orderDto);
}
