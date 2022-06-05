package com.Techeer.Team_C.domain.product.controller;

import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterEditDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterMapper;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterRequestDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterResponseDto;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.product.service.ProductService;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import com.Techeer.Team_C.global.utils.dto.BaseResponseDto;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class ProductController {

    private ProductService productService;
    private ProductRegisterMapper mapper;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

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
        JSONArray jarray = new JSONArray();

        for (int i = 0; i < resultList.size(); i++) {  //한 페이지에 보여줄 결과를 Json 리스트에 넣음
            JSONObject data = new JSONObject(resultList.get(i).toJson());
            jarray.add(data);
        }

        JSONObject obj = new JSONObject();
        obj.put("success", true);
        obj.put("status", 200);
        obj.put("data", jarray);
        return obj.toString();
    }

//    @GetMapping("/list/{id}")
//    @ApiOperation(value = "사용자 등록 상품 목록 조회", notes = "상품 등록 API")
//    public ResponseEntity<List<ProductRegisterResponseDto>> getList(@RequestBody @PathVariable("id") Long userId) {
//
//        return ResponseEntity
//                .ok(productService.registerList(userId).stream().map(mapper::toResponseDto).collect(Collectors.toList()));
//    }

    @PostMapping("/register/{id}")
    @ApiOperation(value = "상품 알림 등록", notes = "상품 알림 등록 API, 헤더에 토큰 정보 필요")
    public ResponseEntity<ProductRegisterResponseDto> save(@RequestBody @Valid final ProductRegisterRequestDto productRegisterRequestDto, @PathVariable("id") Long productId) {

        ProductRegister entity = productService.saveRegister(productRegisterRequestDto, productId);

        return ResponseEntity.ok(mapper.toResponseDto(entity));

//        return ResponseEntity
//                .ok(mapper.toResponseDto(productService.saveRegister(productRegisterRequestDto, productId)));
    }

    @PutMapping("/register/{id}")
    @ApiOperation(value = "상품 알림 등록 정보 수정", notes = "상품 알림 등록 정보 수정 API, 헤더에 토큰 정보 필요")
    public ResponseEntity<ProductRegisterResponseDto> editRegister(@RequestBody @Valid final ProductRegisterEditDto productEditDto, @PathVariable("id") Long productRegisterId) {

//        return ResponseEntity
//                .ok(mapper.toResponseDto(productService.editRegister(productEditDto, productRegisterId)));

//        return ResponseEntity.ok().body(mapper.toResponseDto(productService.editRegister(productEditDto, productRegisterId)));
        return new ResponseEntity<>(mapper.toResponseDto(productService.editRegister(productEditDto, productRegisterId)), HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "상품 알림 등록 삭제", notes = "상품 알림 등록 삭제 API, 헤더에 토큰 정보 필요")
    @DeleteMapping("/register/{id}")
    public ResponseEntity<Void> deleteResister(@PathVariable("id") Long productRegisterId) {
        productService.deleteRegister(productRegisterId);

        return ResponseEntity.noContent().build();
    }


}
