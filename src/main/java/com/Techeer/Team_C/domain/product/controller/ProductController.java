package com.Techeer.Team_C.domain.product.controller;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterRequestDto;
import com.Techeer.Team_C.domain.product.service.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_PREFIX + "/products")
public class ProductController {

    private ProductService productService;


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
    @ApiImplicitParam(
            name = "keyword", value = "검색 할 내용")

    public Object showSearch(@RequestBody @RequestParam("keyword") String keyword,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
                    Pageable page) {

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

    @PostMapping("/register/{id}")
    @ApiOperation(value = "상품 알림 등록", notes = "상품 알림 등록 API, 헤더에 토큰 정보 필요")
    public String add(
            @RequestBody @Valid final ProductRegisterRequestDto productRegisterRequestDto,
            @PathVariable("id") Long productId) {

        productService.addResister(productRegisterRequestDto, productId);
        JSONObject obj = new JSONObject();
        obj.put("success", true);
        obj.put("status", 200);
        return obj.toString();


    }

//    @PutMapping("/register/{id}")
//    @ApiOperation(value = "상품 알림 등록 정보 수정", notes = "상품 알림 등록 정보 수정 API, 헤더에 토큰 정보 필요")
//    public String editResister(
//            @RequestBody @Valid final ProductRegisterRequestDto productRegisterRequestDto,
//            @PathVariable("id") Long productId) {
//
//        productSerivce.editResister(productRegisterRequestDto, productId);
//        JSONObject obj = new JSONObject();
//        obj.put("success", true);
//        obj.put("status", 200);
//        return obj.toString();
//    }

//    @ApiOperation(value = "상품 알림 등록 삭제", notes = "상품 알림 등록 삭제 API, 헤더에 토큰 정보 필요")
//    @DeleteMapping("/register/{id}")
//    public String deleteResister(@PathVariable("id") Long productId) {
//        productSerivce.deleteResister(productId);
//        JSONObject obj = new JSONObject();
//        obj.put("success", true);
//        obj.put("status", 200);
//        return obj.toString();
//    }


}
