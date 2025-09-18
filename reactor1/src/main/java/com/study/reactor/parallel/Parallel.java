package com.study.reactor.parallel;

import com.study.reactor.util.Logger;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

public class Parallel {
    public static void main(String[] args) throws InterruptedException {
        Flux.fromIterable(List.of("a", "b", "c", "d", "e", "f","g","h","i","j","k","a", "b", "c", "d", "e", "f","g","h","i","j","k","a", "b", "c", "d", "e", "f","g","h","i","j","k"))
                .parallel(8)
//                .parallel()
                .runOn(Schedulers.parallel()) // runOn 메소드로만 병렬처리가 가능
                .subscribe(d -> Logger.log("subscribe" + d));

        Thread.sleep(10000);
    }
}
