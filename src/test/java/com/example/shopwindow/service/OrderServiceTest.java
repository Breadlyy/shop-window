package com.example.shopwindow.service;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.entity.Order;
import com.example.shopwindow.entity.OrderItem;
import com.example.shopwindow.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(new BigDecimal("100"));

        when(itemService.findById(1L)).thenReturn(Optional.of(item));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Map<Long, Integer> cart = Map.of(1L, 2);
        Order order = orderService.createOrder(cart);

        assertThat(order.getTotalSum()).isEqualByComparingTo("200");
        assertThat(order.getItems()).hasSize(1);

        OrderItem orderItem = order.getItems().get(0);
        assertThat(orderItem.getCount()).isEqualTo(2);
        assertThat(orderItem.getPrice()).isEqualByComparingTo("200");

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testFindAll() {
        when(orderRepository.findAll()).thenReturn(List.of(new Order(), new Order()));
        assertThat(orderService.findAll()).hasSize(2);
    }

    @Test
    void testFindById() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.findById(1L);
        assertThat(result).isPresent();
    }
}
