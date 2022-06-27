package com.Techeer.Team_C.domain.product.controller;

import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterEditDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterMapper;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterRequestDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterResponseDto;
import com.Techeer.Team_C.domain.product.service.ProductService;
import com.Techeer.Team_C.global.utils.dto.BaseResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX + "/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductRegisterMapper mapper;

    @GetMapping("/{id}")
    @ApiOperation(value = "물품 데이터 조회", notes = "특정 하나의 물품정보 데이터를 조회하는 API")

    public String showDetail(@RequestBody @PathVariable("id") Long productId) {

        Optional<ProductDto> productData = productService.findProduct(productId);

        JSONObject data = new JSONObject(productData.get().toJson());
        JSONObject obj = new JSONObject();
        obj.put("success", true);
        obj.put("status", 200);
        obj.put("data", data);
        return obj.toString();
    }

    @GetMapping("/search")
    @ApiOperation(value = "물품 검색 조회", notes = "검색 내용의 물품리스트를 조회하는 API")
    @ApiImplicitParam(name = "keyword", value = "검색 할 내용")
    public String showSearch(@RequestBody @RequestParam("keyword") String keyword, @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable page) {
        //size : 한 번에 나타날 최대 개수
        //sort : 분류 기준
        List<ProductDto> resultList = productService.pageList(keyword, page);
        Integer totalCount = productService.searchCount(keyword); //총 검색 개수
        JSONArray jarray = new JSONArray();

        for (int i = 0; i < resultList.size(); i++) {  //한 페이지에 보여줄 결과를 Json 리스트에 넣음
            JSONObject data = new JSONObject(resultList.get(i).toJson());
            jarray.add(data);
        }

        JSONObject obj = new JSONObject();
        obj.put("success", true);
        obj.put("status", 200);
        obj.put("data", jarray);
        obj.put("totalCount", totalCount);
        return obj.toString();
    }

    @GetMapping("/list")
    @ApiOperation(value = "사용자 등록 상품 목록 조회", notes = "상품 등록 API, 헤더에 토큰 정보")
    public ResponseEntity<List<ProductRegisterResponseDto>> getList() {
        return ResponseEntity.ok(productService.registerList().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
    }

    @PostMapping("/register/{id}")
    @ApiOperation(value = "상품 알림 등록", notes = "상품 알림 등록 API, 헤더에 토큰 정보 필요")
    public ResponseEntity<ProductRegisterResponseDto> save(@RequestBody @Valid final ProductRegisterRequestDto productRegisterRequestDto, @PathVariable("id") Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponseDto(productService.saveRegister(productRegisterRequestDto, productId)));
    }

    @PatchMapping("/register/{id}")
    @ApiOperation(value = "상품 알림 등록 정보 수정", notes = "상품 알림 등록 정보 수정 API, 헤더에 토큰 정보 필요")
    public ResponseEntity<ProductRegisterResponseDto> editRegister(@RequestBody @Valid final ProductRegisterEditDto productEditDto, @PathVariable("id") Long productId) {
        return ResponseEntity.ok(mapper.toResponseDto(productService.editRegister(productEditDto, productId)));
    }

    @ApiOperation(value = "상품 알림 등록 삭제", notes = "상품 알림 등록 삭제 API, 헤더에 토큰 정보 필요")
    @DeleteMapping("/register/{id}")
    public ResponseEntity<Void> deleteResister(@PathVariable("id") Long productId) {
        productService.deleteRegister(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
