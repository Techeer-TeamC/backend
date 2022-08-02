package com.Techeer.Team_C.domain.product.controller;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.dto.ProductHistoryResponseDto;
import com.Techeer.Team_C.domain.product.dto.ProductPageListResponseDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterEditDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterMapper;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterRequestDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterResponseDto;
import com.Techeer.Team_C.domain.product.entity.Mall;
import com.Techeer.Team_C.domain.product.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_PREFIX + "/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRegisterMapper mapper;

    @GetMapping("/{id}")
    @ApiOperation(value = "물품 데이터 조회", notes = "특정 하나의 물품정보 데이터를 조회하는 API")

    public ResponseEntity<ProductDto> showDetail(@RequestBody @PathVariable("id") Long productId) {

        return ResponseEntity.ok(productService.findProduct(productId));

    }

    @GetMapping("/search")
    @ApiOperation(value = "물품 검색 조회", notes = "검색 내용의 물품리스트를 조회하는 API")
    @ApiImplicitParam(name = "keyword", value = "검색 할 내용")

    public ResponseEntity<ProductPageListResponseDto> showSearch(
        @RequestBody @RequestParam("keyword") String keyword,
        @PageableDefault(size = 9, sort = "name", direction = Sort.Direction.ASC) Pageable page) {
        //size : 한 번에 나타날 최대 개수
        //sort : 분류 기준
        return ResponseEntity.ok(productService.pageList(keyword, page));
    }

    @GetMapping("/list")
    @ApiOperation(value = "사용자 등록 상품 목록 조회", notes = "상품 등록 API, 헤더에 토큰 정보")
    public ResponseEntity<List<ProductRegisterResponseDto>> getList() {
        return ResponseEntity.ok(productService.registerList()
            .stream()
            .map(mapper::toResponseDto)
            .collect(Collectors.toList()));
    }

    @PostMapping("/register")
    @ApiOperation(value = "상품 알림 등록", notes = "상품 알림 등록 API, 헤더에 토큰 정보 필요")
    public ResponseEntity<ProductRegisterResponseDto> save(
        @RequestBody @Valid final ProductRegisterRequestDto productRegisterRequestDto,
        @RequestParam(value = "product") String productName) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapper.toResponseDto(
                productService.saveRegister(productRegisterRequestDto, productName)));
    }

    @PatchMapping("/register/{id}")
    @ApiOperation(value = "상품 알림 등록 정보 수정", notes = "상품 알림 등록 정보 수정 API, 헤더에 토큰 정보 필요")
    public ResponseEntity<ProductRegisterResponseDto> editRegister(
        @RequestBody @Valid final ProductRegisterEditDto productEditDto,
        @PathVariable("id") Long productId) {

        return ResponseEntity.ok(
            mapper.toResponseDto(productService.editRegister(productEditDto, productId)));
    }

    @ApiOperation(value = "상품 알림 등록 삭제", notes = "상품 알림 등록 삭제 API, 헤더에 토큰 정보 필요")
    @DeleteMapping("/register/{id}")
    public ResponseEntity<Void> deleteResister(@PathVariable("id") Long productId) {
        productService.deleteRegister(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @ApiOperation(value = "상품의 몰 정보", notes = "crawling 시 최저가 업데이트 여부 확인을 위함")
    @GetMapping("/product/mallList/{id}")
    public ResponseEntity<List<Mall>> getMallList(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductMallList(productId));
    }

    @ApiOperation(value = "상품의 가격 추이 그래프")
    @GetMapping("/price-history/{id}")
    public ResponseEntity<ProductHistoryResponseDto> getProductHistory(
        @PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductHistory(productId));
    }

    @ApiOperation(value = "월별에 따른 상품의 가격 추이 그래프")
    @GetMapping("/price-history/{id}/month-time")
    public ResponseEntity<ProductHistoryResponseDto> getProductHistoryByTime(
        @PathVariable("id") Long productId, @RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(productService.getProductHistoryForSpecificTime(productId, year, month));
    }

}
