package com.project.springbootproject.controller;

import com.project.springbootproject.dto.cartitemdto.CartItemDto;
import com.project.springbootproject.dto.categorydto.CategoryDto;
import com.project.springbootproject.dto.shoppingcartdto.ShoppingCartDto;
import com.project.springbootproject.model.User;
import com.project.springbootproject.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "ShoppingCart controller",
        description = "Endpoint for managing ShoppingCarts")
@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping(value = "/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;



    @GetMapping
    public ShoppingCartDto getShoppingCart(Principal principal) {
        User user = (User)((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return shoppingCartService.getByUserId(user.getId());
    }

    @PostMapping
    public ShoppingCartDto addBookToShoppingCart(@RequestBody CartItemDto cartItemDto,Principal principal) {
        User user = (User)((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return shoppingCartService.addToCart(cartItemDto, user.getId());
    }

    @PutMapping
    public ShoppingCartDto updateCartItem(@RequestBody CartItemDto cartItemDto) {
        return shoppingCartService.updateCartItem(cartItemDto);
    }

}
