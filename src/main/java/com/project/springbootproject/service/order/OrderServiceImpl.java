package com.project.springbootproject.service.order;

import com.project.springbootproject.dto.order.OrderDto;
import com.project.springbootproject.dto.orderitemdto.OrderItemResponseDto;
import com.project.springbootproject.exception.EntityNotFoundException;
import com.project.springbootproject.mapper.OrderMapper;
import com.project.springbootproject.model.CartItem;
import com.project.springbootproject.model.Order;
import com.project.springbootproject.model.OrderItem;
import com.project.springbootproject.model.ShoppingCart;
import com.project.springbootproject.repository.cartitem.CartItemRepository;
import com.project.springbootproject.repository.order.OrderRepository;
import com.project.springbootproject.repository.orderitem.OrderItemRepository;
import com.project.springbootproject.repository.shoppingcart.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
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
    public Set<OrderItemResponseDto> getAllOrderItemsForOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("Order with id " + orderId + " not found");
        }

        Order order = orderOptional.get();
        Set<OrderItemResponseDto> orderItemResponseDtos = new HashSet<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemResponseDto orderItemResponseDto =
                    orderMapper.orderItemToOrderItemResponseDto(orderItem);
            orderItemResponseDtos.add(orderItemResponseDto);
        }
        return orderItemResponseDtos;
    }

    @Override
    public OrderDto createOrder(String shoppingAddress, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        Order order = new Order();
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(shoppingAddress);
        order.setTotal(BigDecimal.valueOf(shoppingCart.getCartItems()
                .stream()
                .mapToInt(CartItem::getQuantity).sum()));
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        Set<OrderItem> orderItemList = new HashSet<>();

        shoppingCart.getCartItems().stream().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        });
        order.setOrderItems(orderItemList);
        orderRepository.save(order);

        shoppingCartRepository.save(shoppingCart);
        cartItemRepository.deleteCartItemsFromDb(shoppingCart.getId()); //work method


        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAllUserOrders(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemResponseDto> getAllCartItemsForOrder(Long orderId) {
        List<OrderItem> orderItemList = orderItemRepository.findByOrder_Id(orderId);
        return orderItemList.stream()
                .map(orderMapper::orderItemToOrderItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponseDto getOrderItemForSpecificOrder(Long orderId, Long orderItemId) {
        OrderItem byIdAndOrderId = orderItemRepository.findByOrder_IdAndId(orderId, orderItemId);
        return orderMapper.orderItemToOrderItemResponseDto(byIdAndOrderId);
    }
}
