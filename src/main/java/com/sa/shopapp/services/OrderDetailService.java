package com.sa.shopapp.services;

import com.sa.shopapp.dtos.OrderDetailDTO;
import com.sa.shopapp.exceptions.DataNotFoundException;
import com.sa.shopapp.models.Order;
import com.sa.shopapp.models.OrderDetail;
import com.sa.shopapp.models.Product;
import com.sa.shopapp.repositories.OrderDetailRepository;
import com.sa.shopapp.repositories.OrderRepository;
import com.sa.shopapp.repositories.ProductRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException(
                    "cannot find order with id: " + orderDetailDTO.getOrderId()
                ));

        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "cannot find product with id: " + orderDetailDTO.getProductId()
                ));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .numberOfProduct(orderDetailDTO.getNumberOfProduct())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws Exception {
        return orderDetailRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("cannot find order detail with id: " + id)
                );
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        OrderDetail existingOrderDetail = orderDetailRepository
                .findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("cannot find order detail with id: " + id)
                );

        Order existingOrder = orderRepository
                .findById(orderDetailDTO.getOrderId())
                .orElseThrow(()
                        -> new DataNotFoundException("cannot find order with id: " + orderDetailDTO.getOrderId())
                );

        Product existingProduct = productRepository
                .findById(orderDetailDTO.getProductId())
                .orElseThrow(()
                        -> new DataNotFoundException("cannot find product with id: " + orderDetailDTO.getProductId())
                );

        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProduct(orderDetailDTO.getNumberOfProduct());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
