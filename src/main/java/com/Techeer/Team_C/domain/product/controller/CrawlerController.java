package com.Techeer.Team_C.domain.product.controller;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

import com.Techeer.Team_C.domain.product.dto.ProductCrawlingDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterMapper;
import com.Techeer.Team_C.domain.product.service.ProductCrawler;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_PREFIX + "/crawler")
@RequiredArgsConstructor
public class CrawlerController {

    private final ProductCrawler productCrawler;
    private final ProductRegisterMapper mapper;

    @GetMapping("/search")
    public String searchProduct(@RequestParam String item) throws IOException {
        return productCrawler.searchProductPage(item);
    }


    @GetMapping("/product-detail-page")
    public ResponseEntity<ProductCrawlingDto> DanawaDetailpageCrawling(@RequestParam String url) {
        return ResponseEntity.ok(productCrawler.DanawaCrawling(url));
    }
}
