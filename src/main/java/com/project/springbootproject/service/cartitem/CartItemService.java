package com.project.springbootproject.service.cartitem;

import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.model.CartItem;

import java.util.List;

public interface CartItemService {
    List<CartItemDto> findAll();

    CartItemDto addItemToCart(CartItemDto cartItem);

    void updateCarteItmQuantity(Long cartItemId, int quantity, CartItemDto cartItemDto);

    void deleteItemFromCart(Long catItemId);

}
