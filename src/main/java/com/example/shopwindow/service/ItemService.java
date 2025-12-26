package com.example.shopwindow.service;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Flux<Item> findAll() {
        return itemRepository.findAll();
    }

    public Mono<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Flux<Item> reduceCount(Map<Long, Integer> cart) {
        return Flux.fromIterable(cart.entrySet())
                .flatMap(entry ->
                        itemRepository.findById(entry.getKey())
                                .flatMap(item -> {
                                    item.setCount(item.getCount() - entry.getValue());
                                    return itemRepository.save(item);
                                })
                );
    }
}
