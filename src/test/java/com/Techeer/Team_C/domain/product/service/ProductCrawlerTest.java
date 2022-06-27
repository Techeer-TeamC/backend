package com.Techeer.Team_C.domain.product.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProductCrawlerTest {
    ProductCrawler crawler = new ProductCrawler();

    @Test
    public void getProductPage() throws IOException{
        //given
        String url = "http://search.danawa.com/dsearch.php?query=%EB%A7%A5%EB%B6%81+%ED%94%84%EB%A1%9C+14";

        //when
        crawler.searchProductPage(url);

        //then
        System.out.println("move to product page");
    }

}
