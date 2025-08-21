package com.example.shopwindow.service;

import com.example.shopwindow.entity.*;
import com.example.shopwindow.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemService itemService;

    public Order createOrder(Map<Long, Integer> cart) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalSum = new BigDecimal(0);
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Item item = itemService.findById(entry.getKey()).orElseThrow();
            BigDecimal price = item.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
            totalSum = totalSum.add(price);
            orderItems.add(OrderItem.builder()
                    .item(item)
                    .count(entry.getValue())
                    .price(price)
                    .build());
        }
        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .items(orderItems)
                .totalSum(totalSum)
                .build();

        orderItems.forEach(oi -> oi.setOrder(order));
        return orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
}
