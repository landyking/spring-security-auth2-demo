package com.github.landyking;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ShopController {
    @GetMapping("/v1/shop/products")
    @PreAuthorize("hasAuthority('SCOPE_orders:read')")
    public List<String> products() {
        return Arrays.asList("Sugar", "Noodles", "Salt", "Pepper");
    }

    @GetMapping("/v1/shop/orders")
    @PreAuthorize("hasAuthority('SCOPE_orders:read1')")
    public List<String> orders() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Arrays.asList("order-1001", "order-1002", "order-1003");
    }
}
