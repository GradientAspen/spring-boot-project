package com.project.springbootproject.service.order;

import com.project.springbootproject.dto.order.OrderDto;
import com.project.springbootproject.dto.orderitemdto.OrderItemResponseDto;
import com.project.springbootproject.mapper.OrderMapper;
import com.project.springbootproject.model.Order;
import com.project.springbootproject.model.OrderItem;
import com.project.springbootproject.model.ShoppingCart;
import com.project.springbootproject.repository.order.OrderRepository;
import com.project.springbootproject.repository.shoppingcart.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public OrderDto updateOrderStatus(Long id, Order.Status status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        optionalOrder.ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
        return orderMapper.toDto(optionalOrder.get());
    }

    @Override
    public OrderItemResponseDto getAllOrderItems(OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto creatOrder(String shoppingAddress, Long userId) {
        ShoppingCart byUserId = shoppingCartRepository.findByUserId(userId);
        Order order = new Order();
        order.setStatus(Order.Status.PENDING);
        order.setUser(byUserId.getUser());
        order.setOrderDate(LocalDateTime.now());
        Set<OrderItem> orderItemList = new HashSet<>();
        byUserId.getCartItems().stream().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        });
        order.setOrderItems(orderItemList);
        orderRepository.save(order);
        byUserId.setCartItems(new HashSet<>());
        shoppingCartRepository.save(byUserId);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAllUsersOrders(Long userId) {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());

    }


}
