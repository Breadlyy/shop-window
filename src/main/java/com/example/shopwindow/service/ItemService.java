package com.example.shopwindow.service;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.entity.OrderItem;
import com.example.shopwindow.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }
    public void reduceCount(Map<Long, Integer> cart)
    {
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Item item = itemRepository.findById(entry.getKey()).orElseThrow();
            item.setCount(item.getCount() - entry.getValue());
            itemRepository.save(item);
        }
    }
}
