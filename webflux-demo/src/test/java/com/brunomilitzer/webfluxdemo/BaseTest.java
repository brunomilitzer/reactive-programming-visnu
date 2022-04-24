package com.brunomilitzer.webfluxdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BaseTest {

    @Autowired
    private WebClient webClient;

    protected WebClient getWebClient() {
        return this.webClient;
    }

}
