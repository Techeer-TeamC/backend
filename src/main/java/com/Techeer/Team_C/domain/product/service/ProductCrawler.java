package com.Techeer.Team_C.domain.product.service;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.DUPLICATE_PRODUCTREGISTER;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.INVALID_INPUT_VALUE;

import com.Techeer.Team_C.domain.product.dto.MallDto;
import com.Techeer.Team_C.domain.product.dto.ProductCrawlingDto;
import com.Techeer.Team_C.domain.product.dto.ProductListDto;
import com.Techeer.Team_C.domain.product.entity.Mall;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.product.repository.ProductMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductRegisterMysqlRepository;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserMysqlRepository;
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
    private final ProductRegisterMysqlRepository productRegisterMysqlRepository;
    private final UserMysqlRepository userMysqlRepository;

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
        2. 저장되어있는 경우 해당 데이터를 가져와 반환
     */
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
                    Optional<Product> productRegisted = productMysqlRepository.findByName(title);

                    if (productRegisted.isEmpty()){ // DB에 저장되어있는 product가 아닐 때
                        Elements image = doc.select("div.photo_w a img"); // product thumb
                        productDto.setTitle(title);
                        productDto.setImage("http:" + image.attr("src"));

                        Elements mallInfo = doc.select("table.lwst_tbl tbody.high_list").first()
                            .children();
                        for (Element row : mallInfo) {
                            String mallName = row.select("td.mall div a img").attr("alt");
                            String mallLink = row.select("td.mall div a").attr("href");
                            Elements priceInfo = row.select("td.price a span");
                            String paymentOption = priceInfo.select("span.txt_dsc").text();
                            String[] priceSplit = priceInfo.select("span.txt_prc").text().split("원")[0].split(",");
                            String priceStr = "";
                            int price = 0;
                            for (String piece : priceSplit ){
                                priceStr += piece;
                            }
                            if (!priceStr.isEmpty()){
                                price = Integer.parseInt(priceStr);
                            }
                            String deliveryInfo = row.select("td.ship div span.stxt.deleveryBaseSection")
                                .text();
                            int delivery = 0;
                            if ((deliveryInfo.compareTo("무료배송")==1)){
                                String[] split = deliveryInfo.split("원")[0].split(",");
                                String pieces = "";
                                for (String piece : split) {
                                    pieces += piece;
                                }
                                if (!pieces.isEmpty())
                                    delivery = Integer.parseInt(pieces);
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
                        Product product = productRegisted.get();
                        productDto.setTitle(product.getName());
                        productDto.setImage(product.getImage());
                        List<Mall> mallList = product.getMallInfo();
                        for(Mall mall : mallList){
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
            throw new BusinessException(e.getMessage(),INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), INVALID_INPUT_VALUE);
        }
    }


    /*
        product가 DB에 이미 등록되어 있는지
            1. 등록되어 있는 경우, 해당 user가 product를 등록하였는지
                1) user가 등록한 product인 경우, 종료
                2) 등록하지 않은 경우, 해당 product에 user를 ProductRegister로 등록
            2. 등록되지 않은 경우, product & productRegister를 생성하여 DB에 저장

     */
    @Transactional
    public void storeProduct(ProductCrawlingDto productCrawlingDto, int userId, int desirePrice) {
        List<Mall> mallList = new LinkedList<>();
        Optional<Product> entiredProduct = productMysqlRepository.findByName(productCrawlingDto.getTitle());
            if (entiredProduct.isEmpty()){
                for (MallDto mallDto : productCrawlingDto.getMallDtoInfo()){
                    mallList.add(Mall.builder()
                        .name(mallDto.getName())
                        .link(mallDto.getLink())
                        .price(mallDto.getPrice())
                        .delivery(mallDto.getDelivery())
                        .interestFree(mallDto.getInterestFree())
                        .paymentOption(mallDto.getPaymentOption())
                        .build());
                }
                Product product = Product.builder()
                    .name(productCrawlingDto.getTitle())
                    .image(productCrawlingDto.getImage())
                    .mallInfo(mallList)
                    .build();
                User user = userMysqlRepository.getById((long) userId);

                ProductRegister productRegister =
                    ProductRegister.builder()
                        .product(product)
                        .desiredPrice(desirePrice)
                        .status(true)
                        .user(user)
                        .build();
                user.getProductRegister().add(productRegister);
                product.getProductRegister().add(productRegister);
                productMysqlRepository.save(product);
                productRegisterMysqlRepository.save(productRegister);
            }
            else {
                User user = userMysqlRepository.getById((long) userId);

                for (ProductRegister productRegister : entiredProduct.get().getProductRegister()){
                    if (productRegister.getUser().getUserId() == userId){
                        throw new BusinessException("이미 등록한 상품입니다.", DUPLICATE_PRODUCTREGISTER);
                    }
                }
//                ProductRegister productRegister = ProductRegister.builder()
//                entiredProduct.get().getProductRegister().add()
            }
    }

    public List<ProductListDto> productList(String productName) {
        String url = "http://search.danawa.com/dsearch.php?k1="+productName+"&module=goods&act=dispMain";
        List<ProductListDto> productListDtoList = new LinkedList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select(
                "div.main_prodlist.main_prodlist_list ul li[id][class=prod_item]");
            for (Element product : elements) {
                String productUrl = product.select("div div.prod_info p a").attr("href");
                String title = product.select("div div.prod_info p a").text();
                String thumbImageUrl =
                    "http:" + product.select("div div.thumb_image a img").attr("src");
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
            }
            return productListDtoList;
        } catch (IOException e){
            throw new BusinessException(e.getMessage(), INVALID_INPUT_VALUE);
        }
    }
}
