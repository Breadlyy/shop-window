package com.example.shopwindow.controller;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.service.CartService;
import com.example.shopwindow.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<String> getCart(Model model) {
        Map<Long, Integer> cart = cartService.getItems();

        return Flux.fromIterable(cart.entrySet())
                .flatMap(entry -> itemService.findById(entry.getKey())
                        .map(item -> {
                            int qty = entry.getValue();
                            item.setCount(qty);
                            return new Object[]{item, item.getPrice().multiply(BigDecimal.valueOf(qty))};
                        })
                )
                .collectList()
                .map(list -> {
                    List<Item> items = new ArrayList<>();
                    BigDecimal total = BigDecimal.ZERO;

                    for (Object[] obj : list) {
                        items.add((Item) obj[0]);
                        total = total.add((BigDecimal) obj[1]);
                    }

                    model.addAttribute("items", items);
                    model.addAttribute("total", total);
                    model.addAttribute("empty", items.isEmpty());
                    return "cart";
                });
    }

    @PostMapping("/{id}")
    public String updateCartFromCart(@PathVariable Long id, @RequestParam String action) {
        cartService.updateItem(id, action);
        return "redirect:/cart/items";
    }
}
