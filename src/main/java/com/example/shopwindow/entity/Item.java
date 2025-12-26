package com.example.shopwindow.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("items")
public class Item {
    @Id
    private Long id;

    private String title;

    private String description;

    @Column("img_path")
    private String imgPath;

    private int count;

    private BigDecimal price;
}
