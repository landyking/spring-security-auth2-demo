package com.github.landyking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "BACKEND", configuration = CustomBackendConfiguration.class)
public interface BackendClient {
    @GetMapping("/v1/shop/products")
    List<String> products();

    @GetMapping("/v1/shop/orders")
    List<String> orders();
}
