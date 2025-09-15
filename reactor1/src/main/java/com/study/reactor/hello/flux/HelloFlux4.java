package com.study.reactor.hello.flux;

import reactor.core.publisher.Flux;

public class HelloFlux4 {
    public static void main(String[] args) {
        Flux.concat(Flux.just("Venus"),
                Flux.just("Earth"),
                Flux.just("Mars"),
                Flux.just("Hate"))
                .collectList() // 해당 오퍼레이션은 각각의 데이터를 List 형태로 합쳐주는 연산자임.
                                      // public final Mono<List<T>> collectList()
                .subscribe(System.out::println);
    }
}
