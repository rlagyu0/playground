package com.study.reactor.sink;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinkManyReplayAllExample {
    public static void main(String[] args) {
        // 여러개를 방출할 때는 many() 를 사용한다.
        // unicast, multicast, replay 스펙이 있다.
        // replay 는 여러명의 subscriber 에 구독할 수 있는 스펙이다.
        // limit 과 달리 all 은 다시 구독하면 구독 이전에 발행된 모든 데이터를 구독 가능하다
        Sinks.Many<Integer> replay = Sinks.many().replay().all();

        replay.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        replay.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);
        replay.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> flux = replay.asFlux();

        flux.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe1 " + data));

        replay.emitNext(4, Sinks.EmitFailureHandler.FAIL_FAST);
        flux.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe2 " + data));
        /**
         * onNext() | main | 23:32:53.994 |  # subscribe1 1
         * onNext() | main | 23:32:53.996 |  # subscribe1 2
         * onNext() | main | 23:32:53.996 |  # subscribe1 3
         * onNext() | main | 23:32:53.996 |  # subscribe1 4
         * onNext() | main | 23:32:53.997 |  # subscribe2 1
         * onNext() | main | 23:32:53.997 |  # subscribe2 2
         * onNext() | main | 23:32:53.997 |  # subscribe2 3
         * onNext() | main | 23:32:53.997 |  # subscribe2 4
         */
    }
}
