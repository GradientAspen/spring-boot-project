package com.project.springbootproject.repository.orderitem;

import com.project.springbootproject.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    //@Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem>findByOrder_Id(Long orderId);

    OrderItem findByOrder_IdAndId(Long orderId, Long orderItemId);
}
