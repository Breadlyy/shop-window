package com.example.shopwindow.controller;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.service.CartService;
import com.example.shopwindow.service.ItemService;
import com.example.shopwindow.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping("/items/{id}")
    public String updateCartFromItem(@PathVariable Long id, @RequestParam String action) {
        handleCartAction(id, action);
        return "redirect:/items/" + id;
    }

    @GetMapping("/items/{id}")
    public String getItem(@PathVariable Long id, Model model) {
        Item item = itemService.findById(id).orElseThrow();
        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping("/buy")
    public String buyItems() {
        Long orderId = orderService.createOrder(cartService.getItems()).getId();
        itemService.reduceCount(cartService.getItems());
        cartService.clear();
        return "redirect:/orders/" + orderId + "?newOrder=true";
    }

    private void handleCartAction(Long id, String action) {
        cartService.updateItem(id, action);
    }
}
