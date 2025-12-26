package com.example.shopwindow.controller;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.service.CartService;
import com.example.shopwindow.service.ItemService;
import com.example.shopwindow.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping("/items/{id}")
    public String updateCartFromItem(@PathVariable Long id, @RequestParam String action) {
        cartService.updateItem(id, action);
        return "redirect:/items/" + id;
    }

    @GetMapping("/items/{id}")
    public Mono<String> getItem(@PathVariable Long id, Model model) {
        return itemService.findById(id)
                .map(item -> {
                    model.addAttribute("item", item);
                    return "item";
                });
    }

    @PostMapping("/buy")
    public Mono<String> buyItems() {
        return orderService.createOrder(cartService.getItems())
                .flatMap(order -> itemService.reduceCount(cartService.getItems())
                        .then(Mono.fromRunnable(cartService::clear))
                        .thenReturn("redirect:/orders/" + order.getId() + "?newOrder=true")
                );
    }
}
