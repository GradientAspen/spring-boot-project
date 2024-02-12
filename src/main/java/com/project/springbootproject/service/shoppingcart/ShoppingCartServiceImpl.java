package com.project.springbootproject.service.shoppingcart;

import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.mapper.CartItemMapper;
import com.project.springbootproject.mapper.ShoppingCartMapper;
import com.project.springbootproject.model.CartItem;
import com.project.springbootproject.model.ShoppingCart;
import com.project.springbootproject.repository.cartitem.CartItemRepository;
import com.project.springbootproject.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public List<ShoppingCartDto> findAll() {
        return shoppingCartRepository.findAll()
                .stream()
                .map(shoppingCartMapper::toShoppingCartDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShoppingCartDto getById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can not find ShoppingCart with Id: " + id)
        );
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto save(ShoppingCartDto shoppingCartDto) {
        ShoppingCart shoppingCart = shoppingCartMapper.toModel(shoppingCartDto);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public void update(Long id, ShoppingCartDto shoppingCartDto) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);
        ShoppingCart updateCart = shoppingCart.orElseThrow(
                () -> new EntityNotFoundException("Can not find ShoppingCart with ID: " + id)
        );
        shoppingCartMapper.updateShoppingCartFromShoppingCartDto(shoppingCartDto, updateCart);
        shoppingCartRepository.save(updateCart);
    }

    @Override
    public void deleteBiID(Long id) {

    }

    private void addItemToCart(Long cartId, CartItemDto cartItemDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElseThrow(
                () -> new EntityNotFoundException("Can not find ShoppingCart with ID:" + cartId)
        );
        CartItem cartItem = cartItemMapper.toModel(cartItemDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
    }

    public void updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can not find CartItem with ID: " + cartItemId)
        );
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    public void deleteItemFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
