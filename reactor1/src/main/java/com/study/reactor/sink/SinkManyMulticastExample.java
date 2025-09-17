package com.study.reactor.sink;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinkManyMulticastExample {
    public static void main(String[] args) {
        // 여러개를 방출할 때는 many() 를 사용한다.
        // unicast, multicast, replay 스펙이 있다.
        // multicast 는 여러명의 subscriber 에 구독할 수 있는 스펙이다.
        // HotSequence 이다.
        Sinks.Many<Integer> multicast = Sinks.many().multicast().onBackpressureBuffer();

        multicast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        multicast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> flux = multicast.asFlux();

        flux.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe1 " + data));
        // =======================================
        // 이 부분이 1,2 는 이미 subscribe1 이 발행을 받은 상태이다
        // =======================================
        // HotSequence 이기 때문에 이미 구독이 시작되고 데이터 발행을 하다가 중간에 구독을 시작하면 데이터를 받을 수 없다.
        flux.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe2 " + data));

        // 해당 부분은 subscriber 1,2 가 다 데이터를 받지만 1,2 는 subscriber 1 만받을 수 있다.
        multicast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
