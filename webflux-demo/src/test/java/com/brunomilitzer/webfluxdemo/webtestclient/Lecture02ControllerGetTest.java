package com.brunomilitzer.webfluxdemo.webtestclient;

import com.brunomilitzer.webfluxdemo.controller.ParamsController;
import com.brunomilitzer.webfluxdemo.controller.ReactiveMathController;
import com.brunomilitzer.webfluxdemo.dto.Response;
import com.brunomilitzer.webfluxdemo.service.ReactiveMathService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@WebFluxTest(controllers = { ReactiveMathController.class, ParamsController.class })
public class Lecture02ControllerGetTest {

    private final WebTestClient client;

    @MockBean
    private final ReactiveMathService reactiveMathService;

    @Autowired
    public Lecture02ControllerGetTest(WebTestClient client, ReactiveMathService reactiveMathService) {
        this.client = client;
        this.reactiveMathService = reactiveMathService;
    }

    @Test
    public void singleResponseTest() {
        Mockito.when(reactiveMathService.findSquare(Mockito.anyInt())).thenReturn(Mono.empty());

        this.client.get()
                .uri("/reactive-math/square/{input}", 5).exchange().expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(25));
    }

    @Test
    public void listResponseTest() {

        Flux<Response> flux = Flux.range(1, 3).map(Response::new);

        Mockito.when(reactiveMathService.multiplicationTable(Mockito.anyInt())).thenReturn(flux);

        this.client.get()
                .uri("/reactive-math/table/{input}", 5).exchange().expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBodyList(Response.class).hasSize(3);
    }

    @Test
    public void streamingResponseTest() {

        Flux<Response> flux = Flux.range(1, 3).map(Response::new).delayElements(Duration.ofMillis(100));

        Mockito.when(reactiveMathService.multiplicationTable(Mockito.anyInt())).thenReturn(flux);

        this.client.get()
                .uri("/reactive-math/table/{input}/stream", 5).exchange().expectStatus()
                .is2xxSuccessful().expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(Response.class).hasSize(3);
    }

    @Test
    public void paramsTest() {
        Map<String, Integer> map = Map.of("count", 10, "page", 20);

        this.client.get().uri(b -> b.path("/jobs/search")
                .query("count={count}&page={page}").build(map)).exchange().expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class).hasSize(2).contains(10, 20);
    }
}
