package com.sa.shopapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "order_details")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "detail_price")
    @Min(value = 0, message = "price >= 0")
    private Float price;

    @Column(name = "detail_number_of_product")
    @Min(value = 1, message = "number of product >= 1")
    private int numberOfProduct;

    @Column(name = "detail_total_money")
    @Min(value = 0, message = "total price >= 0")
    private Float totalMoney;

    @Column(length = 20)
    private String color;
}
