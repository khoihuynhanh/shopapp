package com.sa.shopapp.services;

import com.sa.shopapp.dtos.OrderDTO;
import com.sa.shopapp.exceptions.DataNotFoundException;
import com.sa.shopapp.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}
