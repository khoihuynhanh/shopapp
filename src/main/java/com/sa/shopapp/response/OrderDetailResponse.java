package com.sa.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sa.shopapp.models.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long order;

    @JsonProperty("product_id")
    private Long product;

    @JsonProperty("detail_price")
    private Float price;

    @JsonProperty("detail_number_of_product")
    private int numberOfProduct;

    @JsonProperty("detail_total_money")
    private Float totalMoney;

    private String color;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .order(orderDetail.getOrder().getId())
                .product(orderDetail.getProduct().getId())
                .price(orderDetail.getPrice())
                .numberOfProduct(orderDetail.getNumberOfProduct())
                .totalMoney(orderDetail.getTotalMoney())
                .color(orderDetail.getColor())
                .build();
        return orderDetailResponse;
    }
}
