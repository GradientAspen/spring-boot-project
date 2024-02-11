package com.project.springbootproject.service.cartitem;

import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.mapper.CartItemMapper;
import com.project.springbootproject.model.CartItem;
import com.project.springbootproject.repository.cartitem.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public List<CartItemDto> findAll() {
        return cartItemRepository.findAll()
                .stream()
                .map(cartItemMapper::toCartItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDto addItemToCart(CartItemDto cartItemDto) {
        CartItem cartItem = cartItemMapper.toModel(cartItemDto);
        return cartItemMapper.toCartItemDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void updateCarteItmQuantity(Long cartItemId, int quantity, CartItemDto cartItemDto) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        CartItem cartItemUpdate = cartItem.orElseThrow(
                () -> new EntityNotFoundException("Can not find CartItem with ID: " + cartItemId)
        );
        cartItemMapper.updateCartItemFromCartItemDto(cartItemDto, cartItemUpdate);
        cartItemRepository.save(cartItemUpdate);
    }

    @Override
    public void deleteItemFromCart(Long catItemId) {
        cartItemRepository.deleteById(catItemId);

    }


}
