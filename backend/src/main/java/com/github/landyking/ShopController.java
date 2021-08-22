package com.github.landyking;

import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ShopController {
    @GetMapping("/v1/shop/products")
    public List<String> products() {
        return Arrays.asList("Sugar", "Noodles", "Salt", "Pepper");
    }

    @GetMapping("/v1/shop/orders")
    @PreFilter("hasAuthority('SCOPE_orders:read')")
    public List<String> orders() {
        return Arrays.asList("order-1001", "order-1002", "order-1003");
    }
}
