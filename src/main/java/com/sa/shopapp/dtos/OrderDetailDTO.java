package com.sa.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "order id >= 1")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "product id >= 1")
    private Long productId;

    @JsonProperty("detail_price")
    @Min(value = 0, message = "price >= 0")
    private Float price;

    @JsonProperty("detail_number_of_product")
    @Min(value = 1, message = "number >= 1")
    private int numberOfProduct;

    @JsonProperty("detail_total_money")
    @Min(value = 0, message = "total >= 0")
    private Float totalMoney;

    private String color;
}
