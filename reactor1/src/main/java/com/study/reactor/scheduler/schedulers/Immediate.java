package com.study.reactor.scheduler.schedulers;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Immediate {
    /**
     * 즉, 결과처럼 .publishOn(Schedulers.parallel()) 에서 생성한 스레드가 filter 를 담당했는데
     * immediate 를 만나면 별도의 스레드를 지정하지 않고  parallel() 에서 생성한
     * 스레드를 이어서 그냥 사용한다.
     * (🚨 바로 직전에 사용한 스레드를 계속해서 사용)
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Flux.range(1, 5)
                .doOnNext(data -> Logger.log("range function : ", data))
                .publishOn(Schedulers.parallel())
                .filter(data -> data > 3)
                .doOnNext(data -> Logger.log("filter function : ", data))
                .publishOn(Schedulers.immediate())
                .map(data -> data * 10)
                .doOnNext(data -> Logger.log("map function : ", data))
                .subscribe(data -> Logger.log("subscribe function : ", data));

        Thread.sleep(10000);
    }
}
