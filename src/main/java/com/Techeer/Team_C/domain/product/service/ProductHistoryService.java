package com.Techeer.Team_C.domain.product.service;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.MALL_NOT_FOUND;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.PRICE_NOT_FOUND;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.UNEXPECTED_MALL;

import com.Techeer.Team_C.domain.alarm.service.AlarmService;
import com.Techeer.Team_C.domain.product.dto.MallDto;
import com.Techeer.Team_C.domain.product.dto.ProductCrawlingDto;
import com.Techeer.Team_C.domain.product.dto.ProductHistoryResponseDto;
import com.Techeer.Team_C.domain.product.dto.ProductHistoryResponseDto.MallPriceHistoryInfo;
import com.Techeer.Team_C.domain.product.entity.Mall;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductHistory;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.product.repository.ProductHistoryMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductMallMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductRegisterMysqlRepository;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {

    private final ProductHistoryMysqlRepository productHistoryMysqlRepository;
    private final ProductMallMysqlRepository productMallMysqlRepository;
    private final ProductMysqlRepository productMysqlRepository;
    private final ProductCrawler productCrawler;
    private final AlarmService alarmService;
    private final ProductRegisterMysqlRepository productRegisterMysqlRepository;

    @Scheduled(cron = "0 */30 * * * *") // 30분마다 실행
    @Transactional
    public void autoUpdate() throws MessagingException {
        List<Product> productList = new ArrayList<>(productMysqlRepository.findAll());
        for (Product product : productList) {   // 저장된 상품에 대해 크롤링 재진행
            ProductCrawlingDto productCrawlingDto = productCrawler.DanawaCrawling(
                product.getUrl());

            List<String> mallNameList = new ArrayList<String>();
            // 상품의 상위 3개 mall 가져오기 (상위 3개)
            for (Mall mall : productMallMysqlRepository.findTop3ByProduct(product).get()) {
                mallNameList.add(mall.getName());
            }
            // 크롤링한 데이터를 히스토리에 저장하기 위한 로직
            for (MallDto mallData : productCrawlingDto.getMallDtoInfo()) {
                if (mallNameList.contains(mallData.getName())) {
                    ProductHistory newHistory = ProductHistory.builder()
                        .product(product)
                        .minimumPrice(mallData.getPrice())
                        .mallName(mallData.getName())
                        .build();
                    productHistoryMysqlRepository.save(newHistory);
                }
            }

            // 아래는 메일 발송을 위한 로직
            Integer latestMinimum = productCrawlingDto.getMinimumPrice(); // 새로 크롤링된 최저값
            for (ProductRegister productRegister : productRegisterMysqlRepository.findByProduct(
                product)) { // 해당 제품을 등록한 사람을 찾고
                if (productRegister.getDesiredPrice() >= latestMinimum) { // 그 사람 요구 가격보다 낮은지
                    alarmService.sendMail(product, productRegister.getUser());
                }
            }
        }
    }


    public ProductHistoryResponseDto getProductHistory(Long productId) {
        ProductHistoryResponseDto productHistoryResponseDto;
        List<MallPriceHistoryInfo> mallHistoryInfoList = new LinkedList<>();

        // product에 저장된 mall 정보 가져오기 (상위 3개)
        Optional<List<Mall>> productMallList = productMallMysqlRepository.findTop3ByProduct(
            Product.builder().productId(productId).build());

        // product에 저장된 mall 정보가 없는 경우
        if (productMallList.isEmpty()) {
            throw new BusinessException("mall 정보가 존재하지 않습니다.", MALL_NOT_FOUND);
        }

        // product 에 대한 가격 추이 그래프 가져오기
        Stack<ProductHistory> productHistoryList =
            productHistoryMysqlRepository.findByProductAndOrderByCreatedDateDesc(
                productId, productMallList.get().size() * 10);

        // product 에 대한 최근 가격 정보가 존재하지 않는 경우
        if (productHistoryList.isEmpty()) {
            throw new BusinessException("product에 대한 최근 가격 데이터가 존재하지 않습니다.", PRICE_NOT_FOUND);
        }

        // 최근 data 기준으로 가장 오래된 created_at 시간 가져오기
        LocalDateTime date = productHistoryList.get(productHistoryList.size() - 1).getCreatedDate();

        // 가격 추이 그래프 데이터 가공
        createPriceHistoryData(productHistoryList, productMallList.get().size(),
            mallHistoryInfoList);

        productHistoryResponseDto = ProductHistoryResponseDto.builder()
            .mallHistoryInfoList(mallHistoryInfoList)
            .date(date)
            .build();

        return productHistoryResponseDto;
    }

    public ProductHistoryResponseDto getProductHistoryForSpecificTime(Long productId,
        int month, int day) {
        ProductHistoryResponseDto productHistoryResponseDto;
        List<MallPriceHistoryInfo> mallHistoryInfoList = new LinkedList<>();

        // product에 저장된 mall 정보 가져오기 (상위 3개)
        Optional<List<Mall>> productMallList = productMallMysqlRepository.findTop3ByProduct(
            Product.builder().productId(productId).build());

        // product에 저장된 mall 정보가 없는 경우
        if (productMallList.isEmpty()) {
            throw new BusinessException("mall 정보가 존재하지 않습니다.", MALL_NOT_FOUND);
        }

        // 특정 시간 대한 가격 추이 그래프 data 가져오기
        Stack<ProductHistory> productHistoryList = productHistoryMysqlRepository.findPriceHistoryForSpecificTime
            (productId, productMallList.get().size(),
                month * 30 * 24 * 6 + day * 24 * 6,
                productMallList.get().size() * 10);

        // product 에 대한 최근 가격 정보가 존재하지 않는 경우
        if (productHistoryList.isEmpty()) {
            throw new BusinessException("product에 대한 최근 가격 데이터가 존재하지 않습니다.", PRICE_NOT_FOUND);
        }

        // 최근 data 기준으로 가장 오래된 created_at 시간 가져오기
        LocalDateTime currentDate = productHistoryList.get(productHistoryList.size() - 1)
            .getCreatedDate();

        // 가격 추이 그래프 데이터 가공
        createPriceHistoryData(productHistoryList, productMallList.get().size(),
            mallHistoryInfoList);

        productHistoryResponseDto = ProductHistoryResponseDto.builder()
            .mallHistoryInfoList(mallHistoryInfoList)
            .date(currentDate)
            .build();

        return productHistoryResponseDto;
    }

    public void createPriceHistoryData(Stack<ProductHistory> productHistoryList, int mallSize,
        List<MallPriceHistoryInfo> mallHistoryInfoList) {

        // priceHistory graph에 전달할 mall을 개별로 분류
        for (int i = 0; i < mallSize && !productHistoryList.isEmpty(); i++) {
            List<Integer> priceList = new LinkedList<>();

            // productHistoryList에서 product의 mall 가져오기
            ProductHistory productHistoryTopMall = productHistoryList.get(i);
            mallHistoryInfoList.add(MallPriceHistoryInfo.builder()
                .mallName(productHistoryTopMall.getMallName())
                .priceList(priceList)
                .build());
        }

        // product mall의 price list 저장
        while (!productHistoryList.isEmpty()) {
            // 오래된 data 순으로 가져오기 (stack 이용)
            ProductHistory productHistory = productHistoryList.pop();
            // 알맞은 mall을 찾기 위한 mallName 비교
            for (int i = 0; i < mallHistoryInfoList.size(); i++) {
                if (mallHistoryInfoList.get(i).getMallName()
                    .equals(productHistory.getMallName())) {
                    // 해당하는 mall의 pricelist에 price 추가
                    mallHistoryInfoList.get(i).
                        getPriceList().add(productHistory.getMinimumPrice());
                    // product의 저장 시점의 mall이 아닌 다른 mall 정보가 priceHistory에 존재할 때
                } else if (i == mallHistoryInfoList.size()) {
                    throw new BusinessException(
                        "priceHistory data의 mall 정보가 mall list의 정보와 일치하지 않습니다.",
                        UNEXPECTED_MALL);
                }
            }
        }

    }

}
