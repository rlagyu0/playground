package com.study.reactor.scheduler.operator.publishon;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class PublishOnEx1 {
    public static void main(String[] args) {
        Flux.range(1, 5)
                .doOnNext(data -> Logger.log("range function : ", data))
                .publishOn(Schedulers.parallel())
                .filter(data -> data > 3)
                .doOnNext(data -> Logger.log("filter function : ", data))
                .publishOn(Schedulers.parallel())
                .map(data -> data * 10)
                .doOnNext(data -> Logger.log("map function : ", data))
                .publishOn(Schedulers.parallel())
                .subscribe(data -> Logger.log("subscribe function : ", data));
    }
}
