package com.project.springbootproject.mapper;

import com.project.springbootproject.config.MapperConfig;
import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.model.CartItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemDto toCartItemDto(CartItem cartItem);

    CartItem toModel(CartItemDto cartItemDto);

}
