package com.Techeer.Team_C.domain.product.dto;

import com.Techeer.Team_C.domain.product.entity.Mall;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Getter
@Setter
public class ProductDto {

    private long productId;

    private String image;

    private String name;

    private List<Mall> mallInfo;

    private boolean status;

    public JSONObject toJson() {
        JSONObject productObj = new JSONObject();
        JSONArray mallArray = new JSONArray();

        for (int i = 0; i < mallInfo.size(); i++) {
            JSONObject data = new JSONObject();
            data.put("name", mallInfo.get(i).getName());
            data.put("link", mallInfo.get(i).getLink());
            data.put("price", mallInfo.get(i).getPrice());
            data.put("delivery", mallInfo.get(i).getDelivery());
            data.put("interestFree", mallInfo.get(i).getInterestFree());
            data.put("paymentOption", mallInfo.get(i).getPaymentOption());
            mallArray.add(data);
        }

        productObj.put("id", productId);
        productObj.put("name", name);
        productObj.put("image", image);
        productObj.put("mallInfo", mallArray);

        return productObj;
    }
}
