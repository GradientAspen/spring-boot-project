package com.project.springbootproject.service.shoppingcart;

import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.dto.cartitemdto.CartItemQuantityDto;
import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.exception.ShoppingCartNotFoundException;
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
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(id);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addToCart(CartItemDto cartItemDto, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart == null) {
            throw new ShoppingCartNotFoundException("Shopping cart not found for user with id: "
                    + userId);
        }
        CartItem cartItem = cartItemMapper.toModel(cartItemDto);
        Book book = bookRepository.getById(cartItemDto.getBookId());
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);

        //added for correct work with cartItemId
        //CartItem savedCartItem = cartItemRepository.save(cartItem);
        //cartItem.setId(savedCartItem.getId());

        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }


    @Override
    public CartItemQuantityDto updateCartItem(Long cartItemId, CartItemQuantityDto quantityDto) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if (!cartItemOptional.isPresent()) {
            throw new EntityNotFoundException("CartItem with id " + cartItemId + " not found");
        }
        CartItem cartItem = cartItemOptional.get();
        cartItem.setQuantity(quantityDto.getQuantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toQuantityDto(cartItem);
    }

    @Override
    public void deleteByIdCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
}
