package com.study.reactor.hello.mono;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.study.reactor.util.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

public class HelloMono3 {
    public static void main(String[] args) {
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("worldtimeapi.org")
                .port(80)
                .path("/api/timezone/Europe")
                .build().encode().toUri();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Mono.just(
                restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<String>(headers),   String.class)
        ).map(
                response -> {
                    DocumentContext jsonContext = JsonPath.parse(response.getBody().toString());
                    String dateTime = jsonContext.read("$.datetime");
                    return dateTime;
                }
        ).subscribe(
                data -> Logger.log("emit data {}", data),
                error -> {},
                () -> Logger.log("emit on complete signal")
        );
    }
}
