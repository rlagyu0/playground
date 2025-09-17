package com.study.reactor.sink;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinkManyReplayLimitExample {
    public static void main(String[] args) {
        // 여러개를 방출할 때는 many() 를 사용한다.
        // unicast, multicast, replay 스펙이 있다.
        // replay 는 여러명의 subscriber 에 구독할 수 있는 스펙이다.
        // 단, 특별한 점은 구독 시점에 limit 의 갯수만큼 최근에 발행된 데이터를 전달 받을 수 있음.
        Sinks.Many<Integer> replay = Sinks.many().replay().limit(2);

        replay.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        replay.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);
        replay.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> flux = replay.asFlux();

        flux.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe1 " + data));
        flux.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe2 " + data));
    }
}
