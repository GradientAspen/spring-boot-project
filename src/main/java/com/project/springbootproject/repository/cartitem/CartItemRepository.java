package com.project.springbootproject.repository.cartitem;

import com.project.springbootproject.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
