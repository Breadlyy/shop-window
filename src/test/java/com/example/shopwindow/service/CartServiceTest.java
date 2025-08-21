package com.example.shopwindow.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
    }

    @Test
    void testAddItemPlus() {
        cartService.updateItem(1L, "plus");
        cartService.updateItem(1L, "plus");

        Map<Long, Integer> items = cartService.getItems();
        assertThat(items).containsEntry(1L, 2);
    }

    @Test
    void testMinusItemRemovesWhenZero() {
        cartService.updateItem(1L, "plus");
        cartService.updateItem(1L, "minus");

        Map<Long, Integer> items = cartService.getItems();
        assertThat(items).doesNotContainKey(1L);
    }

    @Test
    void testDeleteItem() {
        cartService.updateItem(1L, "plus");
        cartService.updateItem(1L, "delete");

        Map<Long, Integer> items = cartService.getItems();
        assertThat(items).doesNotContainKey(1L);
    }

    @Test
    void testClearCart() {
        cartService.updateItem(1L, "plus");
        cartService.updateItem(2L, "plus");

        cartService.clear();
        assertThat(cartService.getItems()).isEmpty();
    }
}
