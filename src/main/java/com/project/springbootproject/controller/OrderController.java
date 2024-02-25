package com.project.springbootproject.controller;

import com.project.springbootproject.dto.order.OrderDto;
import com.project.springbootproject.dto.order.OrderDtoStatus;
import com.project.springbootproject.dto.orderitemdto.OrderDtoAddress;
import com.project.springbootproject.model.User;
import com.project.springbootproject.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(@PathVariable Long id, @RequestBody OrderDtoStatus orderDtoStatus) {
        return orderService.updateOrderStatus(id, orderDtoStatus.getStatus());
    }

    @PostMapping
    public OrderDto creatOrder(@RequestBody OrderDtoAddress orderDtoAddress, Principal principal){
        User user = (User) ((UsernamePasswordAuthenticationToken) principal)
                .getPrincipal();
        return orderService.creatOrder(orderDtoAddress.getAddress(),user.getId());
    }


}
