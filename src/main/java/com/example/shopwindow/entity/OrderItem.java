package com.example.shopwindow.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("order_items")
public class OrderItem {
    @Id
    private Long id;

    private int count;

    private BigDecimal price;

    @Column("item_id")
    private Long itemId;

    @Column("order_id")
    private Long orderId;

    @Transient
    private Item item;
}
