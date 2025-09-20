package com.example.shopwindow.controller;

import com.example.shopwindow.entity.Item;
import com.example.shopwindow.entity.Paging;
import com.example.shopwindow.service.CartService;
import com.example.shopwindow.service.ItemService;
import com.example.shopwindow.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main/items")
public class MainController {
    private final ItemService itemService;
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/")
    public String redirectToMain() {
        return "redirect:/main/items";
    }

    @GetMapping("")
    public Mono<String> listItems(Model model,
                                  @RequestParam(defaultValue = "") String search,
                                  @RequestParam(defaultValue = "NO") String sort,
                                  @RequestParam(defaultValue = "10") int pageSize,
                                  @RequestParam(defaultValue = "1") int pageNumber) {
        return itemService.findAll()
                .collectList()
                .map(items -> {
                    if (!search.isBlank()) {
                        items = items.stream()
                                .filter(i -> i.getTitle().toLowerCase().contains(search.toLowerCase())
                                        || i.getDescription().toLowerCase().contains(search.toLowerCase()))
                                .toList();
                    }

                    switch (sort) {
                        case "ALPHA" -> items = items.stream()
                                .sorted(Comparator.comparing(Item::getTitle))
                                .toList();
                        case "PRICE" -> items = items.stream()
                                .sorted(Comparator.comparing(Item::getPrice))
                                .toList();
                    }

                    int from = Math.max(0, (pageNumber - 1) * pageSize);
                    int to = Math.min(from + pageSize, items.size());
                    List<Item> pageItems = from < items.size() ? items.subList(from, to) : List.of();

                    boolean hasNext = to < items.size();
                    boolean hasPrevious = pageNumber > 1;

                    int rowSize = 3;
                    List<List<Item>> rows = new ArrayList<>();
                    for (int i = 0; i < pageItems.size(); i += rowSize) {
                        rows.add(pageItems.subList(i, Math.min(i + rowSize, pageItems.size())));
                    }

                    model.addAttribute("items", rows);
                    model.addAttribute("search", search);
                    model.addAttribute("sort", sort);
                    model.addAttribute("paging", new Paging(pageNumber, pageSize, hasNext, hasPrevious));
                    return "main";
                });
    }

    @PostMapping("/{id}")
    public String updateCartFromMain(@PathVariable Long id, @RequestParam String action) {
        cartService.updateItem(id, action);
        return "redirect:/main/items";
    }
}
