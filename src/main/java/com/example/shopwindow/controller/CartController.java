package com.example.shopwindow.controller;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.service.CartService;
import com.example.shopwindow.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
public class CartController {
    private final CartService cartService;
    private final ItemService itemService;
    @GetMapping("/")
    public String getCart(Model model) {
        Map<Long, Integer> cart = cartService.getItems();
        List<Item> items = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);

        for (var entry : cart.entrySet()) {
            Item item = itemService.findById(entry.getKey()).orElseThrow();
            int qty = entry.getValue();
            item.setCount(qty);
            total = total.add(item.getPrice().multiply(new BigDecimal(qty)));
            items.add(item);
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("empty", items.isEmpty());
        return "cart";
    }


    // e) POST "/cart/items/{id}"
    @PostMapping("/{id}")
    public String updateCartFromCart(@PathVariable Long id, @RequestParam String action) {
        handleCartAction(id, action);
        return "redirect:/cart/items";
    }
    private void handleCartAction(Long id, String action) {
        cartService.updateItem(id, action);
    }
}
