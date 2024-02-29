package com.project.springbootproject.controller;

import com.project.springbootproject.dto.order.OrderDto;
import com.project.springbootproject.dto.order.OrderDtoStatus;
import com.project.springbootproject.dto.orderitemdto.OrderDtoAddress;
import com.project.springbootproject.dto.orderitemdto.OrderItemResponseDto;
import com.project.springbootproject.model.User;
import com.project.springbootproject.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.List;

@Tag(name = "Order Controller",
        description = "Endpoint for managing Orders")
@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PatchMapping("/{id}")
    @Operation(summary = "Update order Status",
            description = "Update information about order Status for specific Order")
    public OrderDto updateOrderStatus(@PathVariable Long id, @RequestBody OrderDtoStatus orderDtoStatus) {
        return orderService.updateOrderStatus(id, orderDtoStatus.getStatus());
    }

    //  @PostMapping
    //  public OrderDto createOrder(@RequestBody OrderDtoAddress orderDtoAddress, Principal principal){
    //      User user = (User) ((UsernamePasswordAuthenticationToken) principal)
    //              .getPrincipal();
    //      return orderService.createOrder(orderDtoAddress.getAddress(),user.getId());
    //  }

    @PostMapping
    @Operation(summary = "Creat new Order",
            description = "Creat new order for users and after creation order clear shoppingCart")
    public OrderDto createOrder(@RequestBody OrderDtoAddress orderDtoAddress, Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal)
                .getPrincipal();
        return orderService.createOrder(orderDtoAddress.getAddress(), user.getId());
    }

    //  @GetMapping("{orderId}/items")
    //  public Set<OrderItemResponseDto> getAllOrderItem(@PathVariable Long orderId, Principal principal) {
    //      User user = (User) ((UsernamePasswordAuthenticationToken) principal)
    //              .getPrincipal();
    //      return orderService.getAllOrderItemsForOrder(orderId);
    //  }

    @GetMapping
    @Operation(summary = "Get all user's orders",
            description = "Get history of all orders for user")
    public ResponseEntity<List<OrderDto>> getAllUserOrders(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal)
                .getPrincipal();
        List<OrderDto> userOrders = orderService.getAllUserOrders(user.getId());
        return new ResponseEntity<>(userOrders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get cartItem for order",
            description = "Get all cartItem for specific order")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItemsForOrder(@PathVariable Long orderId) {
        List<OrderItemResponseDto> orderItems = orderService.getAllCartItemsForOrder(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get orderItem",
            description = "Get specific orderItem for specific order")
    public ResponseEntity<OrderItemResponseDto> getOrderItemForSpecificOrder(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        OrderItemResponseDto orderItem = orderService.getOrderItemForSpecificOrder(orderId, itemId);
        return ResponseEntity.ok(orderItem);
    }
}
