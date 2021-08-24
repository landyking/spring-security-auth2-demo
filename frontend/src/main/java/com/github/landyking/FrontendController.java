package com.github.landyking;

import com.github.landyking.client.BackendClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class FrontendController {
    @Resource
    BackendClient backendClient;

    @GetMapping("/v1/front/products")
    public List<String> products() {
        return backendClient.products();
    }

    @GetMapping("/v1/front/orders")
    public List<String> orders() {
        return backendClient.orders();
    }
}
