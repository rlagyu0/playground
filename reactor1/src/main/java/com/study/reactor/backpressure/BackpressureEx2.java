package com.study.reactor.backpressure;

import com.study.reactor.util.LogType;
import com.study.reactor.util.Logger;
import com.study.reactor.util.TimeUtil;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class BackpressureEx2 {
    public static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        Flux.range(1, 10)
                .doOnNext(data -> Logger.log(LogType.DO_ON_NEXT,  data))
                .doOnRequest(data -> Logger.log(LogType.DO_ON_REQUEST,  data))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer data) {
                        count ++;
                        Logger.log("# onNext() ======> " + data);

                        if(2 == count) {
                            TimeUtil.sleep(2000L);
                            request(2);
                            count = 0;
                        }
                    }
                });

        Thread.sleep(10000L);
    }
}
