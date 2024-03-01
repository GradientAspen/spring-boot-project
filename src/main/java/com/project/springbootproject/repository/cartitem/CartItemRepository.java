package com.project.springbootproject.repository.cartitem;

import com.project.springbootproject.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("UPDATE CartItem ci SET ci.isDeleted = true WHERE ci.shoppingCart.id = :id")
    void deleteCartItemsFromDb(Long id);
}
