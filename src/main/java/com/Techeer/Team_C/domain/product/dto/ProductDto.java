package com.Techeer.Team_C.domain.product.dto;


import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class ProductDto {

    private long productId;

    private String image;

    private String name;

    private int originPrice;

    private int minimumPrice;

    private String link;

    private String detail;

    private String shipment;

    public JSONObject toJson() { //향후 DB에 저장될 속성값에 따라 명확하게 변경예정. 지금은 간단한 정보만 출력
        JSONObject obj = new JSONObject();
        obj.put("id", productId);
        obj.put("name", name);
        obj.put("price", originPrice);

        return obj;
    }
}

