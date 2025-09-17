package com.study.reactor.sink;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinkManyUnicastExample {
    public static void main(String[] args) {
        // 여러개를 방출할 때는 many() 를 사용한다.
        // unicast, multicast, replay 스펙이 있다.
        // unicast 는 단 하나의 subscriber 에게만 구독할 수 있는 스펙이다.
        Sinks.Many<Integer> unicast = Sinks.many().unicast().onBackpressureBuffer();

        unicast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        unicast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> flux = unicast.asFlux();

        flux.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe1 " + data));

        unicast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);

        // 하지만 unicast 인데 하나 초과하여 두번째 구독자가 구독을 하려고한다면, 아래의 에러 발생
        // Caused by: java.lang.IllegalStateException: Sinks.many().unicast() sinks only allow a single Subscriber
        // flux.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe2 " + data));

    }
}

