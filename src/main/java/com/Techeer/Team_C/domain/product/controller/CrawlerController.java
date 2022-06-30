package com.Techeer.Team_C.domain.product.controller;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

import com.Techeer.Team_C.domain.product.dto.ProductCrawlingDto;
import com.Techeer.Team_C.domain.product.dto.ProductListDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterMapper;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.product.service.ProductCrawler;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @ApiOperation(value = "다나와 페이지의 특정 제품의 상세페이지")
    public ResponseEntity<ProductCrawlingDto> DanawaDetailpage(@RequestParam String detailPageUrl) {
        return ResponseEntity.ok(productCrawler.DanawaCrawling(detailPageUrl));
    }

    @PostMapping("/store-product")
    @ApiOperation(value = "사용자가 등록하려는 제품 저장")
    public ResponseEntity<Void> registerProduct(
        @RequestBody ProductCrawlingDto productCrawlingDto,
        @RequestParam int userId, @RequestParam int desirePrice){
        productCrawler.storeProduct(productCrawlingDto, userId, desirePrice);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/product-list")
    @ApiOperation(value = "사용자가 검색한 제품 리스트 페이지")
    public ResponseEntity<List<ProductListDto>> productList(@RequestParam String productName){
        return ResponseEntity.ok(productCrawler.productList(productName));
    }

}
