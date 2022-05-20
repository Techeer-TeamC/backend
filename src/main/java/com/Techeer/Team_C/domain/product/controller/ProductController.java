package com.Techeer.Team_C.domain.product.controller;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.service.ProductSerivce;
import com.Techeer.Team_C.domain.user.dto.SignupFormDto;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.user.entity.Role;
import com.Techeer.Team_C.domain.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_PREFIX + "/product")
public class ProductController {

    private ProductSerivce productSerivce;


    @Autowired
    public ProductController(ProductSerivce productSerivce) {
        this.productSerivce = productSerivce;
    }

    @GetMapping("/")
    @ApiOperation(value = "물품 데이터 조회", notes = "물품 데이터 조회 API")
    public String showDetail(@RequestBody @RequestParam("id") Long boardId) {

        Optional<ProductDto> productData = productSerivce.findProduct(boardId);

        JSONObject data = new JSONObject(productData.get().toJson());
        JSONObject obj = new JSONObject();
        obj.put("success", true);
        obj.put("status", 200);
        obj.put("data", data);
        return obj.toString();

    }

    @GetMapping("/search")
    public Object showSearch(@RequestBody @RequestParam("keyword") String keyword,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
                    Pageable page) {

        //size : 한 번에 나타날 최대 개수
        //sort : 분류 기준
        List<ProductDto> resultList = productSerivce.pageList(keyword, page);
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


}
