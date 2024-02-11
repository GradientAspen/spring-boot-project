package com.project.springbootproject.dto.shoppingcartdto;

import lombok.Data;

import java.util.Set;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<Long> cartItemIds;
}
