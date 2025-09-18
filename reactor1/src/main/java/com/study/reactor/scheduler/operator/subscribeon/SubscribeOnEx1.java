package com.study.reactor.scheduler.operator.subscribeon;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SubscribeOnEx1 {
    public static void main(String[] args) throws InterruptedException {
        Flux.fromArray(new Integer[] {1,3,5,7})
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(data -> Logger.log("subscribeOn(fromArray) function : ", data))
                .filter(data -> data > 3)
                .doOnNext(data -> Logger.log("filter function : ", data))
                .publishOn(Schedulers.parallel())
                .map(data -> data * 10)
                .doOnNext(data -> Logger.log("map function : ", data))
                .subscribe(data -> Logger.log("subscribe function : ", data));

        Thread.sleep(10000);
    }
}
