package com.example.shopwindow.service;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.entity.Order;
import com.example.shopwindow.entity.OrderItem;
import com.example.shopwindow.repository.ItemRepository;
import com.example.shopwindow.repository.OrderItemRepository;
import com.example.shopwindow.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    private final ItemService itemService;

    /**
     * Создание заказа и сохранение позиций.
     */
    public Mono<Order> createOrder(java.util.Map<Long, Integer> cart) {
        return Flux.fromIterable(cart.entrySet())
                .flatMap(entry -> itemService.findById(entry.getKey())
                        .map(item -> {
                            BigDecimal price = item.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                            return OrderItem.builder()
                                    .itemId(item.getId())
                                    .count(entry.getValue())
                                    .price(price)
                                    .build();
                        }))
                .collectList()
                .flatMap(orderItems -> {
                    BigDecimal totalSum = orderItems.stream()
                            .map(OrderItem::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    Order order = Order.builder()
                            .createdAt(LocalDateTime.now())
                            .totalSum(totalSum)
                            .build();

                    return orderRepository.save(order)
                            .flatMap(savedOrder -> {
                                // сохранить все orderItems с привязкой к orderId
                                return Flux.fromIterable(orderItems)
                                        .flatMap(oi -> {
                                            oi.setOrderId(savedOrder.getId());
                                            return orderItemRepository.save(oi);
                                        })
                                        .then(Mono.just(savedOrder));
                            });
                });
    }

    public Mono<Order> findById(Long id) {
        return orderRepository.findById(id)
                .flatMap(order ->
                        orderItemRepository.findByOrderId(order.getId())
                                .flatMap(oi ->
                                        itemRepository.findById(oi.getItemId())
                                                .map(item -> {
                                                    oi.setItem(item);
                                                    return oi;
                                                })
                                                .defaultIfEmpty(oi)
                                )
                                .collectList()
                                .map(orderItems -> {
                                    order.setItems(orderItems); // теперь есть транзиентное поле
                                    return order;
                                })
                );
    }



    /**
     * Найти все заказы (без подгрузки items).
     * Если нужно с items — можно расширить.
     */
    public Flux<Order> findAll() {
        return orderRepository.findAll();
    }
}
