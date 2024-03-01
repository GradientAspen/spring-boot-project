package com.project.springbootproject.repository.order;

import com.project.springbootproject.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findAllByUserId(@Param("userId") Long userId);
}
