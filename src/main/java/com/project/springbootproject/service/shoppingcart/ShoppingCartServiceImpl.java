package com.project.springbootproject.service.shoppingcart;

import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;
import com.project.springbootproject.mapper.CartItemMapper;
import com.project.springbootproject.mapper.ShoppingCartMapper;
import com.project.springbootproject.model.Book;
import com.project.springbootproject.model.CartItem;
import com.project.springbootproject.model.ShoppingCart;
import com.project.springbootproject.repository.book.BookRepository;
import com.project.springbootproject.repository.cartitem.CartItemRepository;
import com.project.springbootproject.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto getByUserId(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser_Id(id);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addToCart(CartItemDto cartItemDto, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser_Id(userId);
        if (shoppingCart == null) {
            shoppingCart.setCartItems(new HashSet<>());
        }
        CartItem cartItem = cartItemMapper.toModel(cartItemDto);
        Book book = bookRepository.getById(cartItemDto.getBookId());
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateCartItem(CartItemDto cartItemDto) {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemDto.getId());
        if (cartItem.isPresent()) {
            CartItem item = cartItem.get();
            item.setQuantity(cartItemDto.getQuantity());
            cartItemRepository.save(item);
            return shoppingCartMapper.toShoppingCartDto(item.getShoppingCart());
        }
        return null;
    }

    @Override
    public void deleteByIdCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
}
