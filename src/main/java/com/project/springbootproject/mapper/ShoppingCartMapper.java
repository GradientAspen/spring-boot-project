package com.project.springbootproject.mapper;

import com.project.springbootproject.config.MapperConfig;
import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;
import com.project.springbootproject.model.CartItem;
import com.project.springbootproject.model.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItemIds", expression =
            "java(mapCartItemsToIds(shoppingCart.getCartItems()))")
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);

    @Mapping(target = "user.id", source = "userId")
    ShoppingCart toModel(ShoppingCartDto shoppingCartDto);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateShoppingCartFromShoppingCartDto(ShoppingCartDto shoppingCartDto,
                                               @MappingTarget ShoppingCart updateCart);

    default Set<Long> mapCartItemsToIds(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toSet());
    }

}
