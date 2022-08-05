package com.Techeer.Team_C.domain.product.controller;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

import com.Techeer.Team_C.domain.product.dto.ProductHistoryResponseDto;
import com.Techeer.Team_C.domain.product.service.ProductHistoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_PREFIX + "/product-history")
public class ProductHistoryController {

    private final ProductHistoryService productHistoryService;

    @ApiOperation(value = "상품의 가격 추이 그래프")
    @GetMapping("/price-history/{id}")
    public ResponseEntity<ProductHistoryResponseDto> getProductHistory(
        @PathVariable("id") Long productId) {
        return ResponseEntity.ok(productHistoryService.getProductHistory(productId));
    }

    @ApiOperation(value = "월별에 따른 상품의 가격 추이 그래프")
    @GetMapping("/price-history/{productId}/month-time")
    public ResponseEntity<ProductHistoryResponseDto> getProductHistoryByTime(
        @PathVariable("productId") Long productId, @RequestParam int month, @RequestParam int day) {
        return ResponseEntity.ok(
            productHistoryService.getProductHistoryForSpecificTime(productId, month, day));
    }

}
