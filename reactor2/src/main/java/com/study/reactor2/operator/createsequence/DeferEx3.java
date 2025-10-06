package com.study.reactor2.operator.createsequence;

import com.study.reactor2.util.Logger;
import reactor.core.publisher.Mono;

public class DeferEx3 {
    public static void main(String[] args) throws InterruptedException {
        Mono<Object> objectMono = Mono.empty().switchIfEmpty(Mono.defer(() -> sayDefault()));

        Thread.sleep(3000);

        // defer 로 감싼 것은 실제 호출 시점에 콜되기 때문의 근거
        objectMono.subscribe(System.out::println);

    }

    private static Mono<String> sayDefault() {
        Logger.log("Say Default Message");
        return Mono.just("Hi");
    }
}
