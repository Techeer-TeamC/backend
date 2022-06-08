package com.Techeer.Team_C.domain.product.service;

import com.Techeer.Team_C.domain.product.dto.MallDto;
import com.Techeer.Team_C.domain.product.dto.ProductDto;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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

    public String searchProductPage(String item) throws IOException {
        String baseUrl = "http://search.danawa.com/dsearch.php?query=";
        String firstQuery = baseUrl + item;
        Document rawData = (Document) Jsoup.connect(firstQuery).get();

        Elements productElements = rawData.select("a.click_log_product_standard_title_");

        //제일 첫번째 제품 url 링크만 받아오기
        String productUrl = productElements.get(0).attr("abs:href");
        return productUrl;
    }

    public ProductDto DanawaCrawling(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        ProductDto productDto = new ProductDto();
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

                    Elements image = doc.select("div.photo_w a img"); // product thumb
                    String title = doc.select("div.top_summary h3.prod_tit").text();
                    productDto.setTitle(title);
                    productDto.setImage("http:" + image.attr("src"));

                    Elements mallInfo = doc.select("table.lwst_tbl tbody.high_list").first()
                        .children();
                    for (Element row : mallInfo) {
                        String mallLink = row.select("td.mall div a").attr("href");
                        Elements priceInfo = row.select("td.price a span");
                        String cacheOrCard = priceInfo.select("span.txt_dsc").text();
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
                        if (!(deliveryInfo.compareTo("무료배송")==1)){
                            String[] split = deliveryInfo.split("원")[0].split(",");
                            String pieces = "";
                            for (String piece : split) {
                                pieces += piece;
                            }
                            if (!pieces.isEmpty())
                                delivery = Integer.parseInt(pieces);
                        }
                        String interestFree = row.select("td.bnfit div a").text();
                        MallDto mallDto = MallDto.builder().link(mallLink)
                            .price(price)
                            .delivery(delivery)
                            .interestFree(interestFree).build();
                        mallDto.setPaymentOption(cacheOrCard);
                        mallDtoList.add(mallDto);
                    }
                    productDto.setMallDtoInfo(mallDtoList);
                    return response.toString();
                }
            });
            return productDto;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
