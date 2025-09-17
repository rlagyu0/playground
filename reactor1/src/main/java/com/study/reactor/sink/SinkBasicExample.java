package com.study.reactor.sink;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.stream.IntStream;

public class SinkBasicExample {
    public static void main(String[] args) {
        int task = 6;

        // sink 를 만드는 1번 방법
        Flux<Object> flux = Flux.create(sink -> {
            IntStream.range(0, task).forEach(i -> {
                sink.next(i + " task result");
            });
        });

        flux.subscribe(data -> Logger.log(LogType.ON_NEXT, " # subscribe1 " + data));

        //sink 를 만드는 2번 방법
        Sinks.Many<Object> sink = Sinks.many().unicast().onBackpressureBuffer();
        IntStream.range(0, task).forEach(i -> {
            try {
                new Thread(() -> {
                    // 이렇게도 발행이 가능함
                    sink.emitNext(i + " task result", Sinks.EmitFailureHandler.FAIL_FAST);
                }).start();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
