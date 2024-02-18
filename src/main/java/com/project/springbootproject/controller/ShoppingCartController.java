package com.project.springbootproject.controller;

import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.dto.cartitemdto.CartItemQuantityDto;
import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;
import com.project.springbootproject.model.User;
import com.project.springbootproject.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart controller",
        description = "Endpoint for managing ShoppingCarts")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Receive Users ShoppingCart ",
            description = "Receive information about ShoppingCart of User. ")
    public ShoppingCartDto getShoppingCart(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return shoppingCartService.getByUserId(user.getId());
    }

    @PostMapping
    @Operation(summary = "Add book in ShoppingCart",
            description = "Add book in Users ShoppingCart. ")
    public ShoppingCartDto addBookToShoppingCart(@RequestBody CartItemDto cartItemDto,
                                                 Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal)
                .getPrincipal();
        return shoppingCartService.addToCart(cartItemDto, user.getId());
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity of books in Users ShoppingCart ",
            description = "Update  ShoppingCart of User. ")
    public CartItemQuantityDto updateCartItem(
            @PathVariable Long cartItemId, @RequestBody CartItemQuantityDto cartItemQuantityDto) {
        return shoppingCartService.updateCartItem(cartItemId, cartItemQuantityDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete books from Users ShoppingCart ",
            description = "Delete books from ShoppingCart of User. ")
    public void deleteBookFromShoppingCart(@PathVariable Long cartItemId) {
        shoppingCartService.deleteByIdCartItem(cartItemId);
    }
}
