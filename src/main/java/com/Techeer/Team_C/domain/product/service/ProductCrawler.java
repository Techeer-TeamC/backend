package com.Techeer.Team_C.domain.product.service;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.DUPLICATE_PRODUCTREGISTER;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.INVALID_INPUT_VALUE;

import com.Techeer.Team_C.domain.product.dto.MallDto;
import com.Techeer.Team_C.domain.product.dto.ProductCrawlingDto;
import com.Techeer.Team_C.domain.product.dto.ProductListDto;
import com.Techeer.Team_C.domain.product.dto.ProductListResponseDto;
import com.Techeer.Team_C.domain.product.entity.Mall;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.repository.ProductMysqlRepository;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCrawler {

    private final ProductMysqlRepository productMysqlRepository;

    public String searchProductPage(String item) throws IOException {
        String baseUrl = "http://search.danawa.com/dsearch.php?query=";
        String firstQuery = baseUrl + item;
        Document rawData = (Document) Jsoup.connect(firstQuery).get();

        Elements productElements = rawData.select("a.click_log_product_standard_title_");

        //제일 첫번째 제품 url 링크만 받아오기
        String productUrl = productElements.get(0).attr("abs:href");
        return productUrl;
    }

    /*
        product가 이미 DB에 저장되어있는지
        1. 저장되어있지 않을 경우 crawling 하여 product를 생성
        2. 저장되어있는 경우
            2-1. product의 mall 정보 list를 crawling
            2-2. DB product와 crawling data의 최저가 mall 가격정보 비교
                2-2-1. mall은 동일한데 가격만 다를 경우, 가격 업데이트
                2-2-2. mall도 다를 경우, 내림차순으로 가격 정렬..?
        현재는 최저가 정보만 mallList의 첫 번째 mall에 적용, 추후 7/5 회의 때 정하기
     */
    @Transactional
    public ProductCrawlingDto DanawaCrawling(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        ProductCrawlingDto productDto = new ProductCrawlingDto();
        List<MallDto> mallDtoList = new LinkedList<>();
        try {
            httpClient.execute(httpget, new BasicResponseHandler() {
                @Override
                public String handleResponse(HttpResponse response)
                    throws HttpResponseException, IOException {
                    // 웹페이지 한글 처리를 위한 인코딩
                    String res = new String(super.handleResponse(response).getBytes("utf-8"),
                        "utf-8");
                    Document doc = Jsoup.parse(res);

                    String title = doc.select("div.top_summary h3.prod_tit").text();
                    Optional<Product> productRegistered = productMysqlRepository.findByName(title);
                    Elements mallInfo = doc.select("table.lwst_tbl tbody.high_list tr.add_delivery");

                    if (productRegistered.isEmpty()) { // DB에 저장되어있는 product가 아닐 때
                        Elements image = doc.select("div.photo_w a img"); // product thumb
                        productDto.setTitle(title);
                        productDto.setImage("http:" + image.attr("src"));

                        for (Element row : mallInfo) {
                            Element mallNameElement = row.select("td.mall div a").first();
                            String mallName = "";
                            // mall title 정보가 다른 tag에 저장되어있는 case를 위함
                            if (mallNameElement.children().isEmpty()){
                                mallName = mallNameElement.attr("title");
                            }
                            else{
                                mallName = mallNameElement.child(0).attr("alt");
                            }

                            String mallLink = row.select("td.mall div a").attr("href");
                            Elements priceInfo = row.select("td.price a span");
                            String paymentOption = priceInfo.select("span.txt_dsc").text();
                            String[] priceSplit = priceInfo.select("span.txt_prc").text()
                                .split("원")[0].split(",");
                            String priceStr = "";
                            int price = 0;
                            for (String piece : priceSplit) {
                                priceStr += piece;
                            }
                            if (!priceStr.isEmpty()) {
                                price = Integer.parseInt(priceStr);
                            }
                            String deliveryInfo = row.select(
                                    "td.ship div span.stxt.deleveryBaseSection")
                                .text();
                            int delivery = 0;
                            if ((deliveryInfo.compareTo("무료배송") == 1)) {
                                String[] split = deliveryInfo.split("원")[0].split(",");
                                String pieces = "";
                                for (String piece : split) {
                                    pieces += piece;
                                }
                                if (!pieces.isEmpty()) {
                                    delivery = Integer.parseInt(pieces);
                                }
                            }
                            String interestFree = row.select("td.bnfit div a").text();
                            MallDto mallDto = MallDto.builder()
                                .name(mallName)
                                .link(mallLink)
                                .price(price)
                                .delivery(delivery)
                                .interestFree(interestFree).
                                paymentOption(paymentOption).build();
                            mallDtoList.add(mallDto);

                        }
                        productDto.setMallDtoInfo(mallDtoList);

                    } else {    // db에 저장되어있는 product인 경우,
                        Product product = productRegistered.get();
                        Mall registeredMinimumMall = product.getMallInfo().get(0);

                        String [] priceText = doc.select(
                            "div.lowest_top div.row.lowest_price span.lwst_prc a em").text().split(",");
                        String priceStr = "";
                        int price = 0;
                        for (String piece : priceText) {
                            priceStr += piece;
                        }
                        if (!priceStr.isEmpty()) {
                            price = Integer.parseInt(priceStr);
                        }

                        // 영속성에 의해 DB 최저가 자동 update
                        if (registeredMinimumMall.getPrice() > price)
                            registeredMinimumMall.setPrice(price);

//                        for (int i = 0; i<mallInfo.size(); i++) {
//                            Element row = mallInfo.get(i);
//                            Mall mall = registeredMall.get(i);
//                            String mallName = row.select("td.mall div a img").attr("alt");
//                            Elements priceInfo = row.select("td.price a span");
//                            String[] priceSplit = priceInfo.select("span.txt_prc").text()
//                                .split("원")[0].split(",");
//                            String priceStr = "";
//                            int price = 0;
//                            for (String piece : priceSplit) {
//                                priceStr += piece;
//                            }
//                            if (!priceStr.isEmpty()) {
//                                price = Integer.parseInt(priceStr);
//                            }
//
//                            // 최저가 비교
//                            if (mall.getPrice() > price){
//                                //mall 정보 비교
//                                if (mall.getName().equals(mallName)){
//                                    mall.setPrice(price);
//                                }
//                                else{
//                                    String mallLink = row.select("td.mall div a").attr("href");
//                                    String paymentOption = priceInfo.select("span.txt_dsc").text();
//                                    String deliveryInfo = row.select(
//                                            "td.ship div span.stxt.deleveryBaseSection")
//                                        .text();
//                                    int delivery = 0;
//                                    if ((deliveryInfo.compareTo("무료배송") == 1)) {
//                                        String[] split = deliveryInfo.split("원")[0].split(",");
//                                        String pieces = "";
//                                        for (String piece : split) {
//                                            pieces += piece;
//                                        }
//                                        if (!pieces.isEmpty()) {
//                                            delivery = Integer.parseInt(pieces);
//                                        }
//                                    }
//                                    String interestFree = row.select("td.bnfit div a").text();
//
//                                    mall = Mall.builder()
//                                        .name(mallName)
//                                        .link(mallLink)
//                                        .price(price)
//                                        .delivery(delivery)
//                                        .interestFree(interestFree)
//                                        .paymentOption(paymentOption)
//                                        .build();
//                                }
//                                break;
//                            }
//                        }

                        productDto.setTitle(product.getName());
                        productDto.setImage(product.getImage());
                        List<Mall> mallList = product.getMallInfo();
                        for (Mall mall : mallList) {
                            MallDto mallDto = MallDto.builder()
                                .name(mall.getName())
                                .link(mall.getLink())
                                .price(mall.getPrice())
                                .delivery(mall.getDelivery())
                                .interestFree(mall.getInterestFree()).
                                paymentOption(mall.getPaymentOption()).build();
                            mallDtoList.add(mallDto);
                        }
                        productDto.setMallDtoInfo(mallDtoList);
                    }

                    return response.toString();
                }
            });
            return productDto;
        } catch (ClientProtocolException e) {
            throw new BusinessException(e.getMessage(), INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), INVALID_INPUT_VALUE);
        }
    }

    @Transactional
    public Optional<Product> storeProduct(ProductCrawlingDto productCrawlingDto) {
        List<Mall> mallList = new LinkedList<>();
        Optional<Product> registeredProduct = productMysqlRepository.findByName(
            productCrawlingDto.getTitle());

        if (!registeredProduct.isEmpty()) {
            throw new BusinessException("이미 등록한 상품입니다.", DUPLICATE_PRODUCTREGISTER);
        } else {
            Product product = Product.builder()
                .name(productCrawlingDto.getTitle())
                .image(productCrawlingDto.getImage())
                .status(true)
                .build();

            for (MallDto mallDto : productCrawlingDto.getMallDtoInfo()) {
                mallList.add(Mall.builder()
                    .name(mallDto.getName())
                    .link(mallDto.getLink())
                    .price(mallDto.getPrice())
                    .delivery(mallDto.getDelivery())
                    .interestFree(mallDto.getInterestFree())
                    .paymentOption(mallDto.getPaymentOption())
                    .product(product)
                    .build());
            }
            product.setMallInfo(mallList);

            return Optional.of(productMysqlRepository.save(product));
        }
    }

    public ProductListResponseDto productList(String searchWord) {
        String url =
            "http://search.danawa.com/dsearch.php?k1=" + searchWord + "&module=goods&act=dispMain";
        List<ProductListDto> productListDtoList = new LinkedList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select(
                "div.main_prodlist.main_prodlist_list ul li[id][class=prod_item]");
            int totalNubmer = 0;
            for (Element product : elements) {
                String productUrl = product.select("div div.prod_info p a").attr("href");
                String title = product.select("div div.prod_info p a").text();

                String thumbImageUrl = "http:";
                Elements imageUrlElement = product.select("div div.thumb_image a img");
                if (imageUrlElement.hasAttr("src")) {
                    thumbImageUrl += imageUrlElement.attr("src");
                } else {
                    thumbImageUrl += imageUrlElement.attr("data-original");
                }

                Element priceInfoList = product.select("div div.prod_pricelist ul li")
                    .first();
                int price = 0;
                Elements minimumPriceElement = priceInfoList.select("p.price_sect a strong");
                String minimumPrice = minimumPriceElement.text();
                String[] priceSplit = minimumPrice.split(",");
                String priceStr = "";
                for (String piece : priceSplit) {
                    priceStr += piece;
                }
                // 가격 비교 예정인 제품은 0원으로 표기함
                if (!priceStr.isEmpty() && !priceStr.equals("가격비교예정")) {
                    price = Integer.parseInt(priceStr);
                }
                productListDtoList.add(ProductListDto.builder()
                    .title(title)
                    .url(productUrl)
                    .imageUrl(thumbImageUrl)
                    .minimumPrice(price).build());
                totalNubmer++;
            }

            return ProductListResponseDto.builder()
                .totalNumber(totalNubmer)
                .productListDtoList(productListDtoList)
                .build();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), INVALID_INPUT_VALUE);
        }
    }
}
