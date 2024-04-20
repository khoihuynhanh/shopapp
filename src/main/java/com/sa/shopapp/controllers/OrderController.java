package com.sa.shopapp.controllers;

import com.sa.shopapp.dtos.OrderDTO;
import com.sa.shopapp.exceptions.DataNotFoundException;
import com.sa.shopapp.models.Order;
import com.sa.shopapp.services.IOrderService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody
            OrderDTO orderDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.ok("create order " + order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrders(
            @Valid
            @PathVariable("user_id") Long userId
    ) {
        try {
            List<Order> orderList = orderService.findByUserId(userId);
            return ResponseEntity.ok("get order " + orderList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @Valid
            @PathVariable("id") Long orderId
    ) {
        try {
            Order exisitingOrder = orderService.getOrder(orderId);
            return ResponseEntity.ok("lấy ra chi tiết 1 order nào đó " + exisitingOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderDTO orderDTO
    ) throws DataNotFoundException {
        Order order = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok("update order with id: " + order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable Long id
    ) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("delete order");
    }
}
