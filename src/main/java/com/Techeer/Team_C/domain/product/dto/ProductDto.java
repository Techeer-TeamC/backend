package com.Techeer.Team_C.domain.product.dto;


import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class ProductDto {

    private long product_id;

    private String image;

    private String name;

    private int origin_price;

    private int minimum_price;

    private String link;

    private String detail;

    private String shipment;

    public JSONObject toJson() { //향후 DB에 저장될 속성값에 따라 명확하게 변경예정. 지금은 간단한 정보만 출력
        JSONObject obj = new JSONObject();
        obj.put("id", product_id);
        obj.put("name", name);
        obj.put("price", origin_price);

        return obj;
    }
}

