package com.sa.shopapp.services;

import com.sa.shopapp.dtos.OrderDetailDTO;
import com.sa.shopapp.exceptions.DataNotFoundException;
import com.sa.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;
    OrderDetail getOrderDetail(Long id) throws Exception;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws Exception;
    void deleteOrderDetail(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
}
