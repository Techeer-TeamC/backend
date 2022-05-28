package com.Techeer.Team_C.domain.product.controller;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

import com.Techeer.Team_C.domain.product.entity.ProductDto;
import com.Techeer.Team_C.domain.product.service.ProductCrawler;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_PREFIX + "/search")
@RequiredArgsConstructor
public class ProductController {

    private final ProductCrawler productCrawler;

    @GetMapping("/")
    public void searchProduct(@RequestParam String item) throws IOException {
        String productPage = productCrawler.searchProductPage(item);
    }


    @GetMapping("/product-detail-page")
    public ResponseEntity<ProductDto> TestInformation(@RequestParam String url) {
        return ResponseEntity.ok(productCrawler.DanawaCrawling(url));
    }

}
