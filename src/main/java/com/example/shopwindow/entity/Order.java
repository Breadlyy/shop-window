package com.example.shopwindow.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("orders")
public class Order {
    @Id
    private Long id;

    private LocalDateTime createdAt;

    private BigDecimal totalSum;

    @Transient
    private List<OrderItem> items;
}
