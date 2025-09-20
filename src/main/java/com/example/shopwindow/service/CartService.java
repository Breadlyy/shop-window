package com.example.shopwindow.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {
    private final Map<Long, Integer> cart = new ConcurrentHashMap<>();

    public void updateItem(Long itemId, String action) {
        switch (action) {
            case "plus" -> cart.merge(itemId, 1, Integer::sum);
            case "minus" -> cart.computeIfPresent(itemId, (id, count) -> count > 1 ? count - 1 : null);
            case "delete" -> cart.remove(itemId);
        }
    }

    public Map<Long, Integer> getItems() {
        return cart;
    }

    public void clear() {
        cart.clear();
    }
}
