package com.Techeer.Team_C.domain.product.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductHistoryResponseDto {

    private List<MallPriceHistoryInfo> mallHistoryInfoList;
    private LocalDateTime date;

    @Builder
    @Getter
    @Setter
    public static class MallPriceHistoryInfo{
        private String mallName;
        private List<Integer> priceList;
    }
}
