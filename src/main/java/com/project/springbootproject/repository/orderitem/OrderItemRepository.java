package com.project.springbootproject.repository.orderitem;

import com.project.springbootproject.model.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_Id(Long orderId);

    OrderItem findByOrder_IdAndId(Long orderId, Long orderItemId);
}
