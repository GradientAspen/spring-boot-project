package com.project.springbootproject.service.shoppingcart;

import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;

import java.util.List;

public interface ShoppingCartService {
    List<ShoppingCartDto> findAll();

    ShoppingCartDto getById(Long id);

    ShoppingCartDto save(ShoppingCartDto shoppingCartDto);

    void update(Long id, ShoppingCartDto shoppingCartDto);

    void deleteBiID(Long id);
}
