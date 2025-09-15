package com.study.reactor.hello.sequence.hot;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class HelloHotSequence {
    public static void main(String[] args) throws InterruptedException {
        // 핫 시퀀스는 시퀀스가 구독이 시작되고 중간에 다른 구독자가 구독을 하게 되면
        // 구독이 진행중이던 중간 부터 데이터를 소비하기 시작한다.
        // 이 역할을 share 라는 메소드에 집중한다.
        // 콜드 <> 핫 이랑 다른점을  알아두자
        Flux<String> coldFlux
                = Flux.fromIterable(List.of("a", "b", "c","d","e","f","g"))
                .map(String::toUpperCase)
                .delayElements(Duration.ofSeconds(1))
                .share();
        ;

        coldFlux.subscribe(data -> Logger.log(Thread.currentThread().getName() + " subscribe 1 : " + data));
        System.out.println("============================");
        Thread.sleep(5000);
        coldFlux.subscribe(data -> Logger.log(Thread.currentThread().getName()  + " subscribe 2 : " + data));
        Thread.sleep(10000);
    }
}
