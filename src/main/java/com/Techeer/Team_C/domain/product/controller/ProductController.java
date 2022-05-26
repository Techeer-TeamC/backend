package com.Techeer.Team_C.domain.product.controller;

import com.Techeer.Team_C.domain.product.service.ProductCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX + "/search")
@RequiredArgsConstructor
public class ProductController {
    private final ProductCrawler productCrawler;

    @GetMapping("/")
    public void searchProduct(@RequestParam String item) throws IOException {
        String productPage = productCrawler.searchProductPage(item);
    }

    @GetMapping("/test")
    public ResponseEntity<String> TestInformation() {
        return ResponseEntity.ok(productCrawler.DanawaCrawling());
    }

}
