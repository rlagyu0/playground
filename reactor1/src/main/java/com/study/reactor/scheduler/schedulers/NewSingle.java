package com.study.reactor.scheduler.schedulers;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class NewSingle {
    public static void main(String[] args) throws InterruptedException {
        doTask("task1") .subscribe(data -> Logger.log(" subscribe function : ", data));;
        doTask("task2") .subscribe(data -> Logger.log( " subscribe function : ", data));;

        Thread.sleep(10000);
    }

    private static Flux<Integer> doTask(String taskName) {
        return Flux.range(1, 5)
                .doOnNext(data -> Logger.log(taskName + " range function : ", data))
                .publishOn(Schedulers.newSingle("new-single", true))
                .filter(data -> data > 3)
                .doOnNext(data -> Logger.log(taskName + " filter function : ", data))
                .map(data -> data * 10)
                .doOnNext(data -> Logger.log(taskName + " map function : ", data));
    }
}
