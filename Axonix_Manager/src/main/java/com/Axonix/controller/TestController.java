package com.Axonix.controller;

import com.Axonix.service.HuaweiAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final HuaweiAuthService huaweiAuthService;

    @GetMapping("/test-token")
    public String testToken() {
        return huaweiAuthService.getAccessToken();
    }
}