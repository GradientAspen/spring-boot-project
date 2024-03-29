package com.project.springbootproject.mapper;

import com.project.springbootproject.config.MapperConfig;
import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.dto.cartitemdto.CartItemQuantityDto;
import com.project.springbootproject.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemDto toCartItemDto(CartItem cartItem);

    @Mapping(source = "shoppingCartId", target = "shoppingCart.id")
    CartItem toModel(CartItemDto cartItemDto);

    CartItemQuantityDto toQuantityDto(CartItem cartItem);
}
