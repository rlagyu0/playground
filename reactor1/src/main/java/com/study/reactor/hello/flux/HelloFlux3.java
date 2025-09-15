package com.study.reactor.hello.flux;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HelloFlux3 {
    public static void main(String[] args) {
        // 최대 1개만의 데이터 소스를 담을 수 있는 mono 는 데이터를 합칠 수 있다.
        // 최대 1개인 mono 가 두개가 합쳐지면 1개 이상을 담을 수 있는 flux 형태로 반환된다.
        Flux<Object> flux = Mono.justOrEmpty(null)
                .concatWith(Mono.justOrEmpty("jobs"));

        flux.subscribe(System.out::println);
    }
}
