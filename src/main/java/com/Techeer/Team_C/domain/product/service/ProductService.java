package com.Techeer.Team_C.domain.product.service;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.DUPLICATE_PRODUCTREGISTER;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.EMPTY_TOKEN_DATA;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.Mall_NOT_FOUND;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.PRODUCTREGISTER_NOT_FOUND;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.UNEXPECTED_MALL;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.USER_NOT_FOUND;

import com.Techeer.Team_C.domain.alarm.service.AlarmService;
import com.Techeer.Team_C.domain.product.dto.MallDto;
import com.Techeer.Team_C.domain.product.dto.ProductCrawlingDto;
import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.dto.ProductHistoryResponseDto;
import com.Techeer.Team_C.domain.product.dto.ProductHistoryResponseDto.MallPriceHistoryInfo;
import com.Techeer.Team_C.domain.product.dto.ProductPageListResponseDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterEditDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterRequestDto;
import com.Techeer.Team_C.domain.product.entity.Mall;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductHistory;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.product.repository.ProductHistoryMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductMallMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductRegisterMysqlRepository;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMysqlRepository productMysqlRepository;
    private final ProductRegisterMysqlRepository productRegisterMysqlRepository;
    private final ProductCrawler productCrawler;
    private final ModelMapper modelMapper;
    private final ProductMallMysqlRepository productMallMysqlRepository;
    private final UserRepository userRepository;
    private final ProductHistoryMysqlRepository productHistoryMysqlRepository;
    private final AlarmService alarmService;
    private final short priceHistoryMallNumber = 3;

    private ProductDto dtoConverter(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    public ProductDto findProduct(Long id) {

        Optional<Product> product = productMysqlRepository.findById(id);
        if (!product.isPresent()) {
            throw new BusinessException("?????? ?????? ????????? ???????????? ????????????.", PRODUCT_NOT_FOUND);
        }

        return product.map(productEntity -> dtoConverter(productEntity))
            .get();
    }

    public ProductPageListResponseDto pageList(String keyword, Pageable page) {
        Page<Product> lists = productMysqlRepository.findByNameContaining(keyword, page);

        ProductPageListResponseDto result = new ProductPageListResponseDto();

        result.setData(lists.getContent()
            .stream()
            .map(productEntity -> dtoConverter(productEntity))
            .collect(Collectors.toList()));

        result.setTotalCount(productMysqlRepository.countByNameContaining(keyword));

        return result;
    }

    public Integer searchCount(String keyword) {
        return productMysqlRepository.countByNameContaining(keyword);
    }

    public List<ProductRegister> registerList() {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context ??? ?????? ????????? ????????????", EMPTY_TOKEN_DATA);
        }
        Long id = Long.parseLong(authentication.getName());

        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new BusinessException("???????????? ?????? ????????? ?????????.", USER_NOT_FOUND);
        }

        return productRegisterMysqlRepository.findAllByUserAndStatus(userById.get(), true);
    }

    @Scheduled(cron = "0 */30 * * * *") // 30????????? ??????
    @Transactional
    public void autoUpdate() throws MessagingException {
        List<Product> productList = new ArrayList<>(productMysqlRepository.findAll());
        for (Product product : productList) {   // ????????? ????????? ?????? ????????? ?????????
            ProductCrawlingDto productCrawlingDto = productCrawler.DanawaCrawling(
                product.getUrl());

            List<String> mallNameList = new ArrayList<String>();
            for (ProductHistory productHistory : productHistoryMysqlRepository.findTop3ByProduct(
                product)) {
                mallNameList.add(productHistory.getMallName());
            }
            // ???????????? ???????????? ??????????????? ???????????? ?????? ??????
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

            // ????????? ?????? ????????? ?????? ??????
            Integer latestMinimum = productCrawlingDto.getMinimumPrice(); // ?????? ???????????? ?????????
            for (ProductRegister productRegister : productRegisterMysqlRepository.findByProduct(
                product)) { // ?????? ????????? ????????? ????????? ??????
                if (productRegister.getDesiredPrice() >= latestMinimum) { // ??? ?????? ?????? ???????????? ?????????
                    alarmService.sendMail(product, productRegister.getUser());
                }
            }
        }
    }

    @Transactional
    public ProductRegister saveRegister(ProductRegisterRequestDto productRegisterRequestDto,
        String productName) {

        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context ??? ?????? ????????? ????????????", EMPTY_TOKEN_DATA);
        }
        Long id = Long.parseLong(authentication.getName());

        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new BusinessException("???????????? ?????? ????????? ?????????.", USER_NOT_FOUND);
        }
        Optional<Product> productByName = productMysqlRepository.findByName(productName);
        if (!productByName.isPresent()) {
            productByName = productCrawler.storeProduct(
                productCrawler.DanawaCrawling(productRegisterRequestDto.getUrl()));
        }
        Optional<ProductRegister> productRegisterById = productRegisterMysqlRepository.findByUserAndProduct(
            userById.get(), productByName.get());
        if (productRegisterById.isPresent()) {
            ProductRegister entity = productRegisterById.get();
            if (!entity.isStatus()) {
                entity.update(productRegisterRequestDto.getDesiredPrice(), true);
                return entity;
            } else {
                throw new BusinessException("?????? ????????? ???????????????.", DUPLICATE_PRODUCTREGISTER);
            }
        }

        int minimumPrice = productByName.get()
            .getMallInfo()
            .get(0)
            .getPrice();

        ProductRegister productRegister = productRegisterMysqlRepository.build(userById.get(),
            productByName.get(), productRegisterRequestDto.getDesiredPrice(), minimumPrice, true);
        productRegisterMysqlRepository.save(productRegister);

        return productRegister;
    }

    @Transactional
    public ProductRegister editRegister(ProductRegisterEditDto productRegisterEditDto,
        Long productId) {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context ??? ?????? ????????? ????????????", EMPTY_TOKEN_DATA);
        }
        Long id = Long.parseLong(authentication.getName());

        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new BusinessException("???????????? ?????? ????????? ?????????.", USER_NOT_FOUND);
        }
        Optional<ProductRegister> productRegisterById = productRegisterMysqlRepository.findByUserAndProduct(
            userById.get(), productMysqlRepository.findById(productId)
                .get());

        if (!productRegisterById.isPresent()) {
            throw new BusinessException("???????????? ?????? ?????? ?????????.", PRODUCTREGISTER_NOT_FOUND);
        }

        ProductRegister entity = productRegisterById.get();
        entity.update(productRegisterEditDto.getDesiredPrice(), true);

        productRegisterMysqlRepository.save(entity);
        return entity;
    }

    @Transactional
    public void deleteRegister(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context ??? ?????? ????????? ????????????", EMPTY_TOKEN_DATA);
        }

        Long userId = Long.parseLong(authentication.getName());
        Optional<User> userById = userRepository.findById(userId);
        if (!userById.isPresent()) {
            throw new BusinessException("???????????? ?????? ??????????????????.", USER_NOT_FOUND);
        }

        Optional<Product> productById = productMysqlRepository.findById(productId);

        if (!productById.isPresent()) {
            throw new BusinessException("???????????? ?????? ???????????????.", PRODUCT_NOT_FOUND);
        }

        Optional<ProductRegister> productRegisterById = productRegisterMysqlRepository.findByUserAndProduct(
            userById.get(), productById.get());
        productRegisterById.ifPresentOrElse(productRegister -> {
            productRegisterById.get()
                .setStatus(false);
            productRegisterMysqlRepository.save(productRegister);
        }, () -> {
            throw new BusinessException("???????????? ?????? ???????????????.", PRODUCTREGISTER_NOT_FOUND);
        });

    }

    public List<Mall> getProductMallList(Long productId) {
        Optional<Product> productById = productMysqlRepository.findById(productId);
        if (!productById.isPresent()) {
            throw new BusinessException("???????????? ?????? ???????????????.", PRODUCT_NOT_FOUND);
        }

        Optional<List<Mall>> RegisteredProductMallList =
            productMallMysqlRepository.findAllByProduct(
                Product.builder().
                    productId(productId)
                    .build());
        if (!RegisteredProductMallList.isPresent()) {
            throw new BusinessException("mall ????????? ???????????? ????????????.", Mall_NOT_FOUND);
        }

        return RegisteredProductMallList.get();
    }

    public ProductHistoryResponseDto getProductHistory(Long productId) {
        ProductHistoryResponseDto productHistoryResponseDto;
        List<MallPriceHistoryInfo> mallHistoryInfoList = new LinkedList<>();

        // product??? priceHistory ?????? ?????? Mall data ????????????
        Stack<ProductHistory> productHistoryList =
            productHistoryMysqlRepository.findTop30ByProductAndOrderByCreatedDateDesc(
                productId);

        // ?????? 10?????? ???????????? ?????? ????????? ???????????? created_at ?????? ????????????
        LocalDateTime date = productHistoryList.get(productHistoryList.size() - 1).getCreatedDate();

        // priceHistory graph??? ????????? mall ??????
        for (int i = 0; i < priceHistoryMallNumber && !productHistoryList.isEmpty(); i++) {
            List<Integer> priceList = new LinkedList<>();

            // productHistoryList?????? product??? mall ????????????
            ProductHistory productHistoryTopMall = productHistoryList.get(i);
            mallHistoryInfoList.add(MallPriceHistoryInfo.builder()
                .mallName(productHistoryTopMall.getMallName())
                .priceList(priceList)
                .build());
        }
        // product mall??? price list ??????
        while (!productHistoryList.isEmpty()) {
            // ????????? data ????????? ???????????? (stack ??????)
            ProductHistory productHistory = productHistoryList.pop();
            for (int i = 0; i < mallHistoryInfoList.size(); i++) {
                // ????????? mall??? ?????? ?????? mallName ??????
                if (mallHistoryInfoList.get(i).getMallName()
                    .equals(productHistory.getMallName())) {
                    // ???????????? mall??? pricelist??? price ??????
                    mallHistoryInfoList.get(i).
                        getPriceList().add(productHistory.getMinimumPrice());
                    // product??? ?????? ????????? mall??? ?????? ?????? mall ????????? priceHistory??? ????????? ???
                } else if (i == mallHistoryInfoList.size()) {
                    throw new BusinessException("priceHistory data??? mall ????????? ???????????? ????????????.",
                        UNEXPECTED_MALL);
                }
            }
        }

        productHistoryResponseDto = ProductHistoryResponseDto.builder()
            .mallHistoryInfoList(mallHistoryInfoList)
            .date(date)
            .build();

        return productHistoryResponseDto;
    }
}
