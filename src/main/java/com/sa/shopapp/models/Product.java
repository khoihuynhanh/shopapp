package com.sa.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Table(name = "products")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", length = 350, nullable = false)
    private String name;

    private Float price;

    @Column(name = "thumbnail", length = 300, nullable = true)
    private String thumbnail;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
