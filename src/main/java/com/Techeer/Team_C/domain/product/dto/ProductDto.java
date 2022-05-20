package com.Techeer.Team_C.domain.product.dto;


import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class ProductDto {
    private long product_id;

    private String product_image;

    private String name;

    private int origin_price;

    private int minimum_price;

    private String product_link;

    private String product_detail;

    private String shipment;

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("id", product_id);
        obj.put("name", name);
        obj.put("price",origin_price);

        return obj;
    }
}

