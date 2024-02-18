package com.project.springbootproject.service.shoppingcart;

import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.dto.cartitemdto.CartItemQuantityDto;
import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto getByUserId(Long id);

    ShoppingCartDto addToCart(CartItemDto cartItemDto, Long userId);

    CartItemQuantityDto updateCartItem(Long cartIdemId, CartItemQuantityDto quantityDto);

    void deleteByIdCartItem(Long id);

}
