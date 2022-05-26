package com.Techeer.Team_C.domain.product.service;

import java.util.Iterator;
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

import java.io.IOException;

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

    public String DanawaCrawling(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://prod.danawa.com/info/?pcode=15461990&keyword=%EB%A7%A5%EB%B6%81%ED%94%84%EB%A1%9C+m1&cate=112758");
        try {
            httpClient.execute(httpget, new BasicResponseHandler() {
                @Override
                public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
                    // 웹페이지 한글 처리를 위한 인코딩
                    String res = new String(super.handleResponse(response).getBytes("8859_1"),
                        "euc-kr");
                    Document doc = Jsoup.parse(res);
                    Elements rows = doc.select("table.lwst_tbl tbody");
                    StringBuilder builder = new StringBuilder();
                    for (Element row : rows) {
                        Iterator<Element> iterElem = row.getElementsByTag("tr").iterator();
                        for (int i = 1; iterElem.hasNext(); i++){
                            Element priceClass = iterElem.next().child(1);
                            Iterator<Element> priceTag = priceClass.getElementsByClass("prc_t").iterator();
                            String priceValue = priceTag.next().childNode(0).toString();
                            builder.append(i + ". : " + priceValue + "   \t");
                            System.out.println(builder.toString());
                        }
                    }
                    return builder.toString();
                }
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
