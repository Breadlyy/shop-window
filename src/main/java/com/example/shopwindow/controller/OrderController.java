package com.example.shopwindow.controller;

import com.example.shopwindow.entity.Order;
import com.example.shopwindow.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/")
    public String getOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrder(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean newOrder,
            Model model
    ) {
        Order order = orderService.findById(id).orElseThrow();
        model.addAttribute("order", order);
        model.addAttribute("id", order.getId());
        model.addAttribute("items", order.getItems());
        model.addAttribute("newOrder", newOrder);
        return "order";
    }
}
