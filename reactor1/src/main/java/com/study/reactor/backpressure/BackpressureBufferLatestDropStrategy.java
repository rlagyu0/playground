package com.study.reactor.backpressure;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import com.study.reactor.util.TimeUtil;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * BUFFER-LATEST
 * 소비자에게 전달할 데이터가 버퍼에 가득 차면 버퍼안 데이터부터 DROP 시키는 전략
 * (버퍼 안의 신입 먼저 드랍시킴)
 * 이 말은 즉슨, 버퍼 안에 들어있는 기준이 아니라 생상자로부터 EMIT 된 데이터 기준
 *
 */
public class BackpressureBufferLatestDropStrategy {
    public static void main(String[] args) {
        Flux
                .interval(Duration.ofMillis(300L))
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, " # emmited by original flux : " + data))
                .onBackpressureBuffer(2,
                        dropped -> Logger.log("# Overflow & dropped : " + dropped),
                        BufferOverflowStrategy.DROP_LATEST)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, " # emmited by buffer : " + data))
                .publishOn(Schedulers.parallel(), false, 1)
                .subscribe(
                        data -> {
                            TimeUtil.sleep(1000L);
                            Logger.log(LogType.ON_NEXT, data);
                        },
                        error -> Logger.log(LogType.ON_ERROR)
                );

        TimeUtil.sleep(10000L);
    }
}
