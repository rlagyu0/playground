package com.study.reactor.backpressure;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import com.study.reactor.util.TimeUtil;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * 버퍼가 가득 찼을 경우
 *  .onBackpressureDrop(dropped -> Logger.log( "# dropped : " + dropped))
 *  를 통해서 버퍼 밖에 있는 데이터 중 가장 오래된 대기하는 데이터를 DROP
 */
public class BackpressureDropStrategy {
    public static void main(String[] args) {
        Flux
                .interval(Duration.ofMillis(1))
                .onBackpressureDrop(dropped -> Logger.log( "# dropped : " + dropped))
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
