package com.study.reactor.backpressure;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import com.study.reactor.util.TimeUtil;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackpressureErrorStrategy {
    public static void main(String[] args) {
        // 소비속도 : 5
        // 생산속도 : 1
        // 생산 속도가 앞도적으로 커서 에러가 발생한다.
        // 단순히 버퍼가 가득 찼을 때 에러를 발생시키는 전략
        /**
         * .
         * .
         * .
         * onNext() | parallel-1 | 00:47:16.167 | 253
         * onNext() | parallel-1 | 00:47:16.173 | 254
         * onNext() | parallel-1 | 00:47:16.179 | 255
         * onERROR() | parallel-1 | 00:47:16.179
         */
        Flux.interval(Duration.ofMillis(1))
                .onBackpressureError()
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
                // publishOn -> 스레드를 추가적으로 할당 함.
                .publishOn(Schedulers.parallel())
                .subscribe(data -> {
                        TimeUtil.sleep(5L);
                        Logger.log(LogType.ON_NEXT, data);
                    },
                    error -> {Logger.log(LogType.ON_ERROR);}
                );

        TimeUtil.sleep(6000);

    }
}
