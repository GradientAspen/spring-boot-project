package com.project.springbootproject.service.shoppingcart;

import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;
import com.project.springbootproject.model.User;

public interface ShoppingCartService {

    ShoppingCartDto getByUserId(Long id);

    ShoppingCartDto addToCart(CartItemDto cartItemDto, Long userId);

    ShoppingCartDto updateCartItem(CartItemDto cartItemDto); //fix params

    void deleteByIdCartItem(Long id); // fix params check for Users

}
