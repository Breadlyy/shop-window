package com.example.shopwindow.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {
    private final Map<Long, Integer> cart = new HashMap<>();

    public void updateItem(Long itemId, String action) {
        switch (action) {
            case "plus" -> cart.put(itemId, cart.getOrDefault(itemId, 0) + 1);
            case "minus" -> {
                cart.computeIfPresent(itemId, (id, count) -> count > 1 ? count - 1 : null);
            }
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
