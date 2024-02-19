package com.project.springbootproject.repository.shoppingcart;

import com.project.springbootproject.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    ShoppingCart findByUserId(Long id);
}
