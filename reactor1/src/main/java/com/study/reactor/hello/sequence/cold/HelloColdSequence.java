package com.study.reactor.hello.sequence.cold;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class HelloColdSequence {
    public static void main(String[] args) throws InterruptedException {
        // 콜드 시퀀스는 해당 시퀀스가 각자 다른 구독자가 구독을 언제하던 상관 없이
        // 처음부터 구독을 진행한다.
        Flux<String> coldFlux
                = Flux.fromIterable(List.of("a", "b", "c","d","e","f","g"))
                .map(String::toUpperCase)
                .delayElements(Duration.ofSeconds(1));
        ;

        coldFlux.subscribe(data -> Logger.log(Thread.currentThread().getName() + " subscribe 1 : " + data));
        System.out.println("============================");
        Thread.sleep(5000);
        coldFlux.subscribe(data -> Logger.log(Thread.currentThread().getName()  + " subscribe 2 : " + data));
        Thread.sleep(10000);
    }
}
