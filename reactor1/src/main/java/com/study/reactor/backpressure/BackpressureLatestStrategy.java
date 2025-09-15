package com.study.reactor.backpressure;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import com.study.reactor.util.TimeUtil;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * 소비자에게 전달할 데이터가 버퍼에 가득 차면 버퍼 밖에서 대기
 * (버퍼 밖에서 있는 가장 생긴지 얼마 안된 데이터를)
 * 하는 데이터를 선순위로 버퍼에 채운다
 *
 * ** 버퍼에 채워지는 시점에 버퍼에 채워지는 데이터보다 오래 버퍼 밖에 머무르면 그것은 폐기 **
 */
public class BackpressureLatestStrategy {
    public static void main(String[] args) {
        Flux
                .interval(Duration.ofMillis(1))
                .onBackpressureLatest()
                //                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT, data))
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
